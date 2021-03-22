package com.fjkyly.paradise.other

import android.Manifest.permission
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.ContentUris
import android.content.ContentValues
import android.graphics.Color
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import androidx.annotation.RequiresPermission
import com.fjkyly.paradise.base.App
import java.util.*

/**
 * 日历提醒工具类
 */
object CalendarReminderUtils {

    // 日历 URL
    private const val CALENDER_URL = "content://com.android.calendar/calendars"

    // 日历事件 URL
    private const val CALENDER_EVENT_URL = "content://com.android.calendar/events"

    // 日历提醒 URL
    private const val CALENDER_REMINDER_URL = "content://com.android.calendar/reminders"

    // 日历名称
    private const val CALENDARS_NAME = "boohee"

    // 日历的账户名称
    private const val CALENDARS_ACCOUNT_NAME = "BOOHEE@boohee.com"

    // 日历的账户类型
    private const val CALENDARS_ACCOUNT_TYPE = "com.android.boohee"

    // 日历显示名称
    private const val CALENDARS_DISPLAY_NAME = "BOOHEE账户"

    /**
     * 检查是否已经添加了日历账户，如果没有则先添加一个日历账户再查询，
     * 获取账户成功返回账户 ID ，否则返回 -1
     */
    private fun checkAndAddCalendarAccount(): Int {
        val oldId = checkCalendarAccount()
        return if (oldId >= 0) {
            oldId
        } else {
            val addId = addCalendarAccount()
            if (addId >= 0) checkCalendarAccount() else -1
        }
    }

    /**
     * 检查是否存在现有账户，存在则返回账户id，否则返回 -1
     */
    private fun checkCalendarAccount(): Int {
        val context = App.appContext
        val userCursor =
            context.contentResolver.query(Uri.parse(CALENDER_URL), null, null, null, null)
        userCursor ?: return -1
        val count = userCursor.count
        return if (count > 0) {
            // 存在现有账户，取第一个账户的 id 返回
            userCursor.moveToFirst()
            val id = userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID))
            userCursor.close()
            id
        } else {
            -1
        }
    }

    /**
     * 添加日历账户，账户创建成功则返回账户 ID ，否则返回 -1
     */
    private fun addCalendarAccount(): Long {
        val context = App.appContext
        val timeZone = TimeZone.getDefault()
        val values = ContentValues()
        values.run {
            // 日历的名称
            put(CalendarContract.Calendars.NAME, CALENDARS_NAME)
            // 用于将条目同步到设备的帐户。如果account_type不是{@link ACCOUNT_TYPE_LOCAL}，则名称和类型必须与设备上的帐户匹配，否则日历将被删除
            put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME)
            // 用于将条目同步到设备的帐户的类型。如果设备上没有匹配的帐户，则类型为{@link ACCOUNT_TYPE_LOCAL}的事件表将被删除
            put(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE)
            // 日历的显示名称
            put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDARS_DISPLAY_NAME)
            // 是否选择要显示的日历？ 0-不显示与此日历关联的事件。 1-显示与此日历相关的事件
            put(CalendarContract.Calendars.VISIBLE, 1)
            // 日历的颜色。这只能由同步适配器（而不是其他应用程序）更新，因为更改日历的颜色可能会对其显示产生不利影响
            put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE)
            // 用户对日历的访问级别
            put(
                CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
                CalendarContract.Calendars.CAL_ACCESS_OWNER
            )
            // 此日历是否已同步，并且其事件是否存储在设备上？ 0-不同步此日历或​​存储该日历的事件。 1-同步此日历的事件
            put(CalendarContract.Calendars.SYNC_EVENTS, 1)
            //日历所关联的时区
            put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.id)
            // 该日历的所有者帐户（基于日历供稿）。这与委派日历的_SYNC_ACCOUNT不同
            put(CalendarContract.Calendars.OWNER_ACCOUNT, CALENDARS_ACCOUNT_NAME)
            // 组织者可以对活动做出回应吗？如果否，则UI不应显示组织者的状态。默认值为1
            put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0)
        }
        var calendarUri = Uri.parse(CALENDER_URL)
        calendarUri = calendarUri.buildUpon().apply {
            appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
            appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME)
            appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE)
        }.build()
        val resultUri = context.contentResolver.insert(calendarUri, values)
        return if (resultUri == null) -1 else ContentUris.parseId(resultUri)
    }

    /**
     * 插入日历事件
     * 需要先获取系统日历读写权限
     * reminderTime：提醒事件的时间戳
     * previousDate：提前 previousDate 天有提醒
     * 返回值：为 true 表示添加成功，为 false 表示添加失败
     */
    @SuppressLint("NewApi")
    @RequiresPermission(anyOf = [permission.WRITE_CALENDAR, permission.READ_CALENDAR])
    fun insertCalendarEvent(
        title: String,
        description: String,
        reminderTime: Long,
        previousDate: Int
    ): Boolean {
        // 此处已经经过权限检查了
        deleteCalendarEventByEventTitle(title = title)
        val context = App.appContext
        // 获取日历账户的 ID
        val calendarId = checkAndAddCalendarAccount()
        // 获取到的账户 ID 无效，直接返回，添加日历失败
        if (calendarId < 0) return false
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        // 设置开始的时间
        calendar.timeInMillis = reminderTime
        val startTimeInMillis = calendar.timeInMillis
        // 设置终止时间，开始时间加 10 分钟
        calendar.timeInMillis =
            startTimeInMillis + ((AlarmManager.INTERVAL_FIFTEEN_MINUTES / 15) * 10)
        val endTimeInMillis = calendar.timeInMillis
        val eventValues = ContentValues()
        eventValues.run {
            put("title", title);
            put("description", description);
            put("calendar_id", calendarId); // 插入账户的 ID
            put(CalendarContract.Events.DTSTART, startTimeInMillis)
            put(CalendarContract.Events.DTEND, endTimeInMillis)
            put(CalendarContract.Events.HAS_ALARM, 1)// 设置有闹钟提醒
            put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai")// 这个是时区，必须有
        }
        val newEvent = context.contentResolver.insert(Uri.parse(CALENDER_EVENT_URL), eventValues)
        // 添加日历失败，直接返回
        newEvent ?: return false
        // 事件提醒的设定
        val remindValues = ContentValues()
        remindValues.run {
            put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(newEvent))
            // 提前 previousDate 天有提醒
            put(
                CalendarContract.Reminders.MINUTES,
                previousDate * 24 * 60
            )
            // 设置提醒方式
            put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
            val uri = context.contentResolver.insert(Uri.parse(CALENDER_REMINDER_URL), remindValues)
            val isSuccess = uri != null
            Log.d(
                TAG,
                "insertCalendarEvent: ===>Alarm reminder added ${if (isSuccess) "成功" else "失败"}"
            )
            return isSuccess
        }
    }

    /**
     * 根据日历事件的名称删除日历事件
     * 需要先获取系统日历读写权限
     */
    @RequiresPermission(anyOf = [permission.WRITE_CALENDAR, permission.READ_CALENDAR])
    fun deleteCalendarEventByEventTitle(title: String) {
        val context = App.appContext
        val eventCursor =
            context.contentResolver.query(Uri.parse(CALENDER_EVENT_URL), null, null, null, null)
        // 查询返回空值
        eventCursor ?: return
        if (eventCursor.count > 0) {
            // 遍历所有事件，找到 title 跟需要查询的 title 一样的项
            eventCursor.moveToFirst()
            // 如果当前游标不是最后一个，则循环
            while (eventCursor.isAfterLast.not()) {
                val eventTitle = eventCursor.getString(eventCursor.getColumnIndex("title"))
                if (title.isNotEmpty() && title == eventTitle) {
                    val id =
                        eventCursor.getInt(eventCursor.getColumnIndex(CalendarContract.Calendars._ID)) // 取得id
                    val deleteUri = ContentUris.withAppendedId(
                        Uri.parse(CALENDER_EVENT_URL),
                        id.toLong()
                    )
                    val rows = context.contentResolver.delete(deleteUri, null, null)
                    if (rows == -1) { // 事件删除失败
                        return
                    }
                }
                // 游标移动到下一行
                eventCursor.moveToNext()
            }
        }
        eventCursor.close()
    }

    @Deprecated("暂未实现，请不要调用该方法！")
    @RequiresPermission(anyOf = [permission.WRITE_CALENDAR, permission.READ_CALENDAR])
    private fun queryCalendarEvents() {
        val context = App.appContext
        val eventCursor =
            context.contentResolver.query(Uri.parse(CALENDER_EVENT_URL), null, null, null, null)
        // 查询返回空值
        eventCursor ?: return
        if (eventCursor.count > 0) {
            eventCursor.moveToFirst()
            val rows = mapOf<String, Any>()
            val columns = mapOf<String, Any>()
            // 如果不是最后一行，则继续循环
            while (eventCursor.isAfterLast.not()) {
                // 将游标移动到下一行
                eventCursor.moveToNext()
            }
        }
        eventCursor.close()
    }

    private const val TAG = "CalendarReminderUtils"
}