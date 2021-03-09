package com.fjkyly.paradise.other

import android.Manifest
import android.content.ContentUris
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.provider.CalendarContract
import android.text.TextUtils
import android.util.Log
import androidx.core.app.ActivityCompat
import com.fjkyly.paradise.base.App
import com.paul.eventreminder.CalendarManager
import com.paul.eventreminder.model.CalendarEvent
import com.paul.eventreminder.utils.TimeUtil
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CalendarUtils {

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val sdf2 = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    // 是否开启提醒
    private var isAlarm = false

    // 提前提醒的分钟数
    private var mAlarmTime = 15

    // 是否重复
    private var isRepeat = false

    private val mContext = App.appContext

    /**
     * 设置是否提醒
     */
    fun setAlarm(alarm: Boolean): CalendarUtils {
        isAlarm = alarm
        return this
    }

    /**
     * 是否提醒
     */
    fun isAlarm() = isAlarm

    /**
     * 设置提前多久提醒，以分钟为单位
     */
    fun setAlarmTime(alarmTime: Int): CalendarUtils {
        mAlarmTime = alarmTime
        return this
    }

    /**
     * 获取提前多久提醒，以分钟为单位
     */
    fun getAlarmTime() = mAlarmTime

    /**
     * 设置是否重复提醒
     */
    fun setRepeat(repeat: Boolean): CalendarUtils {
        isRepeat = repeat
        return this
    }

    /**
     * 获取是否重复提醒
     */
    fun isRepeat() = isRepeat

    /**
     * 检查是否已经添加了日历账户，如果没有添加先添加一个日历账户再查询
     * 获取账户成功返回账户id，否则返回-1
     */
    private fun checkAndAddCalendarAccount(): Int {
        try {
            val oldId: Int = checkCalendarAccount()
            return if (oldId >= 0) {
                oldId
            } else {
                val addId: Long = addCalendarAccount()
                if (addId >= 0) {
                    checkCalendarAccount()
                } else {
                    -1
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return -1
    }

    /**
     * 检查是否存在现有账户，存在则返回账户id，否则返回-1
     */
    private fun checkCalendarAccount(): Int {
        mContext.contentResolver
            .query(Uri.parse(CALENDER_URL), null, null, null, null).use { userCursor ->
                if (userCursor == null) { //查询返回空值
                    return -1
                }
                val count = userCursor.count
                return if (count > 0) { //存在现有账户，取第一个账户的id返回
                    userCursor.moveToFirst()
                    val id =
                        userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID))
                    userCursor.close()
                    id
                } else {
                    -1
                }
            }
    }

    /**
     * 返回所有的日历账户
     */
    private fun listCalendarAccount(): List<Map<String, String>> {
        val result = mutableListOf<Map<String, String>>()
        mContext.contentResolver
            .query(Uri.parse(CALENDER_URL), null, null, null, null).use { userCursor ->
                if (userCursor != null && userCursor.count > 0) {
                    userCursor.moveToFirst()
                    while (!userCursor.isAfterLast) {
                        val account =
                            userCursor.getString(userCursor.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME))
                        val name =
                            userCursor.getString(userCursor.getColumnIndex(CalendarContract.Calendars.DEFAULT_SORT_ORDER))
                        val calId =
                            userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID))
                        val map: MutableMap<String, String> =
                            HashMap()
                        map["name"] = name
                        map["account"] = account
                        map["calId"] = calId.toString()
                        result.add(map)
                        userCursor.moveToNext()
                    }
                    userCursor.close()
                }
            }
        if (result.isEmpty()) {
            val addId: Long = addCalendarAccount()
            if (addId >= 0) {
                val map = mapOf(
                    "name" to "新增账户",
                    "account" to "新增账户",
                    "calId" to addId.toString()
                )
                result.add(map)
            }
        }
        return result
    }

    /**
     * 添加日历账户，账户创建成功则返回账户id，否则返回-1
     */
    private fun addCalendarAccount(): Long {
        val timeZone = TimeZone.getDefault()
        val value = ContentValues()
        value.put(CalendarContract.Calendars.NAME, CALENDARS_NAME)
        value.put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME)
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE)
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDARS_DISPLAY_NAME)
        value.put(CalendarContract.Calendars.VISIBLE, 1)
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE)
        value.put(
            CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
            CalendarContract.Calendars.CAL_ACCESS_OWNER
        )
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1)
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.id)
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, CALENDARS_ACCOUNT_NAME)
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0)
        var calendarUri = Uri.parse(CALENDER_URL)
        calendarUri = calendarUri.buildUpon()
            .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
            .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME)
            .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE)
            .build()
        val result = mContext.contentResolver.insert(calendarUri, value)
        // 获取事件ID
        return if (result == null) -1 else ContentUris.parseId(result)
    }

    private fun addScheduleToCalender(
        calId: Int,
        model: CalendarEvent,
        curWeek: Int,
        listener: OnExportProgressListener?
    ): Boolean {
        val weekSet: MutableSet<Int> = HashSet()
        weekSet.addAll(model.weekList)
        val startTime = model.startTime
        val endTime = model.endTime
        val thisDay = TimeUtil.getTodayWeekNumber()
        val prefix = ""
        for (i in weekSet) {
            val date = TimeUtil.getTargetDate(curWeek, i, thisDay, model.dayOfWeek)
            val dateString = sdf.format(date)
            try {
                val realStartDate = sdf2.parse("$dateString $startTime")
                val realEndDate = sdf2.parse("$dateString $endTime")
                realStartDate ?: return false
                realEndDate ?: return false
                addCalendarEvent(
                    calId,
                    model.summary,
                    model.content + "@" + CALENDARS_ACCOUNT_NAME,
                    model.loc,
                    realStartDate, realEndDate, listener
                )
            } catch (e: ParseException) {
                e.printStackTrace()
                if (listener != null) {
                    listener.onError(e.message)
                    return false
                }
            }
        }
        return true
    }

    /**
     * 添加日历事件
     */
    private fun addCalendarEvent(
        calId: Int,
        title: String,
        description: String,
        location: String,
        startDate: Date,
        endDate: Date,
        listener: OnExportProgressListener?
    ) {
        if (calId < 0) { // 获取账户id失败直接返回，添加日历事件失败
            listener?.onError("添加日历账户失败，可能没有授予日历权限或者没有日历账户")
            return
        }
        val event = ContentValues()
        event.put("title", title)
        event.put("description", description)
        event.put("calendar_id", calId) // 插入账户的 id
        event.put(CalendarContract.Events.EVENT_LOCATION, location)
        event.put(CalendarContract.Events.DTSTART, startDate.time)
        // TODO: 2021-03-09 可以在这里设置事件的重复规则
        event.put(CalendarContract.Events.DTEND, endDate.time)
        event.put(CalendarContract.Events.HAS_ALARM, 0) // 设置有闹钟提醒,1：有提醒
        event.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai") // 这个是时区，必须有
        val newEvent = mContext.contentResolver
            .insert(Uri.parse(CALENDER_EVENT_URL), event) // 添加事件
        val id = if (newEvent == null) -1 else ContentUris.parseId(newEvent)
        if (id != -1L && isAlarm) {
            // 开始组装事件提醒数据
            val reminders = ContentValues()
            // 此提醒所对应的事件ID
            reminders.put(CalendarContract.Reminders.EVENT_ID, id)
            // 设置提醒提前的时间(0：准时  -1：使用系统默认)
            reminders.put(CalendarContract.Reminders.MINUTES, mAlarmTime)
            // 设置事件提醒方式为通知警报
            reminders.put(
                CalendarContract.Reminders.METHOD,
                CalendarContract.Reminders.METHOD_ALERT
            )
            if (ActivityCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.WRITE_CALENDAR
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                listener?.onError("没有权限")
                return
            }
            val uri = mContext.contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, reminders)
        }
        if (newEvent == null) { // 添加日历事件失败直接返回
            listener?.onError("添加日历日程失败")
        }
    }

    fun queryAllEvent() {
        mContext.contentResolver.query(Uri.parse(CALENDER_EVENT_URL), null, null, null, null)
            .use { eventCursor ->
                eventCursor ?: return
                if (eventCursor.count > 0) {
                    val columnNames: Array<String> = eventCursor.columnNames
                    //遍历所有事件，找到title跟需要查询的title一样的项
                    eventCursor.moveToFirst()
                    while (eventCursor.moveToNext()) {
                        Log.d(TAG, "=============================================")
                        for (columnName in columnNames) {
                            Log.d(
                                TAG,
                                "field ===> " + columnName + "value===> " + eventCursor.getString(
                                    eventCursor.getColumnIndex(columnName)
                                )
                            )
                        }
                        Log.d(TAG, "=============================================")
                    }
                }
                eventCursor.close()
            }
    }

    /**
     * 删除日历事件
     */
    fun deleteCalendarEvent(
        title: String
    ) {
        mContext.contentResolver
            .query(Uri.parse(CALENDER_EVENT_URL), null, null, null, null)
            .use { eventCursor ->
                eventCursor ?: return
                if (eventCursor.count > 0) {
                    //遍历所有事件，找到title跟需要查询的title一样的项
                    eventCursor.moveToFirst()
                    while (eventCursor.isAfterLast.not()) {
                        val eventTitle =
                            eventCursor.getString(eventCursor.getColumnIndex("title"))
                        if (!TextUtils.isEmpty(title) && title == eventTitle) {
                            val id =
                                eventCursor.getInt(eventCursor.getColumnIndex(CalendarContract.Calendars._ID)) //取得id
                            val deleteUri = ContentUris.withAppendedId(
                                Uri.parse(CALENDER_EVENT_URL),
                                id.toLong()
                            )
                            val rows =
                                mContext.contentResolver.delete(deleteUri, null, null)
                            if (rows == -1) { // 事件删除失败
                                return
                            }
                        }
                        eventCursor.moveToNext()
                    }
                }
                eventCursor.close()
            }
    }

    /**
     * 删除日历事件
     * @param listener 监听器，实现回调
     */
    fun deleteCalendarEvent(listener: CalendarManager.OnExportProgressListener?) {
        mContext.contentResolver.query(Uri.parse(CALENDER_EVENT_URL), null, null, null, null)
            .use { eventCursor ->
                if (eventCursor == null) { // 查询返回空值
                    listener?.onError("找不到任何事件")
                    return
                }
                if (eventCursor.count > 0) {
                    //遍历所有事件，找到title跟需要查询的title一样的项
                    var i = 0
                    eventCursor.moveToFirst()
                    while (eventCursor.isAfterLast.not()) {
                        i++
                        val description: String =
                            eventCursor.getString(eventCursor.getColumnIndex("description"))
                        if (!TextUtils.isEmpty(description) && description.endsWith("@$CALENDARS_ACCOUNT_NAME")) {
                            val id: Int =
                                eventCursor.getInt(eventCursor.getColumnIndex(CalendarContract.Calendars._ID)) // 取得id
                            val deleteUri = ContentUris.withAppendedId(
                                Uri.parse(CALENDER_EVENT_URL),
                                id.toLong()
                            )
                            val rows: Int = mContext.contentResolver.delete(deleteUri, null, null)
                            listener?.onProgress(eventCursor.count, i)
                            if (rows == -1) { // 事件删除失败
                                listener?.onError("事件删除失败")
                                return
                            }
                        }
                        eventCursor.moveToNext()
                    }
                }
                eventCursor.close()
            }
        listener?.onSuccess()
    }

    interface OnExportProgressListener {
        fun onSuccess()
        fun onProgress(total: Int, now: Int) {}
        fun onError(msg: String?)
    }

    /**
     * 添加多个日历事件
     * @param mySubjects 内容
     * @param listener 监听器，实现回调
     */
    fun addCalendarEvent(
        mySubjects: List<CalendarEvent>,
        curWeek: Int,
        listener: OnExportProgressListener?
    ) {
        var count = 0
        for (calendarEvent in mySubjects) {
            count++
            listener?.onProgress(mySubjects.size, count)
            val result = addScheduleToCalender(
                checkAndAddCalendarAccount(), calendarEvent,
                curWeek, listener
            )
            if (!result) {
                return
            }
        }
    }

    /**
     * 添加单个日历事件
     * @param mySubject 内容 不能为空，为空的话你添加个啥？
     * @param listener 监听器，实现回调
     */
    fun addCalendarEvent(
        mySubject: CalendarEvent,
        curWeek: Int,
        listener: OnExportProgressListener?
    ) {
        if (addScheduleToCalender(checkAndAddCalendarAccount(), mySubject, curWeek, listener)) {
            listener?.onSuccess()
        }
    }

    companion object {

        private const val TAG = "CalendarUtils"

        // 日历 URL
        private const val CALENDER_URL = "content://com.android.calendar/calendars"

        // 日历事件 URL
        private const val CALENDER_EVENT_URL = "content://com.android.calendar/events"

        // 日历提醒 URL
        private const val CALENDER_REMINDER_URL = "content://com.android.calendar/reminders"

        // 日历名称
        private const val CALENDARS_NAME = "吃药提醒"

        // 日历的账户名称
        private const val CALENDARS_ACCOUNT_NAME = "康养乐园"

        // 日历的账户类型
        private const val CALENDARS_ACCOUNT_TYPE = "com.fjkyly.android"

        // 日历显示名称
        private const val CALENDARS_DISPLAY_NAME = "康养乐园APP账户"
    }
}