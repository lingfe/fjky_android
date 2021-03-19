package com.fjkyly.paradise.other

import android.Manifest
import android.app.AlarmManager
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.provider.CalendarContract
import android.text.TextUtils
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.contentValuesOf
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
                    return checkCalendarAccount()
                } else {
                    -1
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "checkAndAddCalendarAccount: ===>请务必授予日历权限")
            e.printStackTrace()
            return -1
        }
    }

    /**
     * 检查是否存在现有账户，存在则返回账户id，否则返回-1
     */
    private fun checkCalendarAccount(): Int {
        mContext.contentResolver
            .query(CalendarContract.Calendars.CONTENT_URI, null, null, null, null)
            .use { userCursor ->
                if (userCursor == null) { // 查询返回空值
                    return -1
                }
                val count = userCursor.count
                return if (count > 0) { // 存在现有账户，取第一个账户的 id 返回
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
            .query(CalendarContract.Calendars.CONTENT_URI, null, null, null, null)
            .use { cursor ->
                if (cursor != null && cursor.count > 0) {
                    cursor.moveToFirst()
                    while (!cursor.isAfterLast) {
                        val account =
                            cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME))
                        val name =
                            cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.DEFAULT_SORT_ORDER))
                        val calId =
                            cursor.getInt(cursor.getColumnIndex(CalendarContract.Calendars._ID))
                        val map: MutableMap<String, String> =
                            HashMap()
                        map["name"] = name
                        map["account"] = account
                        map["calId"] = calId.toString()
                        result.add(map)
                        cursor.moveToNext()
                    }
                    cursor.close()
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
        calendarEvent: CalendarEvent,
        curWeek: Int,
        listener: OnExportProgressListener?
    ): Boolean {
        val weekSet: MutableSet<Int> = HashSet()
        weekSet.addAll(calendarEvent.weekList)
        // 如果未设置周数，则默认只在当前周开始设置
        if (weekSet.isEmpty()) weekSet.add(0)
        val startTime = calendarEvent.startTime
        val endTime = calendarEvent.endTime
        val thisDay = TimeUtil.getTodayWeekNumber()
        val prefix = ""
        for (i in weekSet) {
            val date = TimeUtil.getTargetDate(curWeek, i, thisDay, calendarEvent.dayOfWeek)
            val dateString = sdf.format(date)
            try {
                val realStartDate = sdf2.parse("$dateString $startTime")
                val realEndDate = sdf2.parse("$dateString $endTime")
                realStartDate ?: return false
                realEndDate ?: return false
                addCalendarEvent(
                    calId,
                    calendarEvent.summary,
                    calendarEvent.content + "@" + CALENDARS_ACCOUNT_NAME,
                    calendarEvent.loc,
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
        if (calId < 0) { // 获取账户 id 失败直接返回，添加日历事件失败
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
        if (isRepeat.not()) {
            // 不重复，仅一次
            event.put(CalendarContract.Events.DTEND, endDate.time)
        } else {
            // 每天都重复
            event.put(CalendarContract.Events.RRULE, EVERY_DAY_RRLUE)
            event.put(CalendarContract.Events.DURATION, "P60S")
        }
        event.put(CalendarContract.Events.HAS_ALARM, 1) // 设置有闹钟提醒,1：有提醒
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
            val uri =
                mContext.contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, reminders)
        }
        if (newEvent == null) { // 添加日历事件失败直接返回
            listener?.onError("添加日历日程失败")
        }
    }

    fun queryAllEvent(block: (result: String) -> Unit) {
        val strBuilder = StringBuilder()
        mContext.contentResolver
            .query(Uri.parse(CALENDER_EVENT_URL), null, null, null, null)
            .use { cursor ->
                cursor ?: return
                if (cursor.count > 0) {
                    //遍历所有事件
                    cursor.moveToFirst()
                    val columnNames = cursor.columnNames
                    while (cursor.isAfterLast.not()) {
                        for (columnName in columnNames) {
                            val value =
                                cursor.getString(cursor.getColumnIndex(columnName))
                            strBuilder.append("key：$columnName，value：$value\n")
                            // Log.d(TAG, "queryAllEvent: ===>key：$columnName，value：$value")
                        }
                        // val eventTitle =
                        //     cursor.getString(cursor.getColumnIndex("title"))
                        // Log.d(TAG, "queryAllEvent: ===>title：$eventTitle")
                        cursor.moveToNext()
                    }
                }
                cursor.close()
            }
        block(strBuilder.toString())
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
                    // 遍历所有事件，找到 title 跟需要查询的 title 一样的项
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

    /**
     * 此方法主要用于查询字段信息
     *
     * 1、先检查日历账户是否存在，如果不存在则添加一个日历账户
     * 2、查询所有记录
     * 3、回调操作信息
     */
    private fun operateCalendarEvent(
        block: (contentResolver: ContentResolver, eventCursor: Cursor, columnNames: Array<out String>) -> Unit
    ) {
        val code = checkAndAddCalendarAccount()
        // 如果返回 -1 则代表失败，无法进行后续操作，直接返回
        if (code == -1) {
            return
        }
        // 查询所有的系统日历事件
        val contentResolver = mContext.contentResolver
        contentResolver
            .query(CalendarContract.Events.CONTENT_URI, null, null, null, null)
            .use { eventCursor ->
                eventCursor ?: return
                // 如果记录有一条或更多，则继续遍历，否则就不用遍历了
                if (eventCursor.count > 0) {
                    // 将游标移动到第一行
                    eventCursor.moveToFirst()
                    // 获取所有字段名
                    val columnNames = eventCursor.columnNames
                    // 如果当前游标不是在最后一行，则遍历，否则不继续遍历
                    while (eventCursor.isAfterLast.not()) {
                        // 可以根据自己的业务需求进行操作
                        block(contentResolver, eventCursor, columnNames)
                        // 将游标移动到下一行
                        eventCursor.moveToNext()
                    }
                }
                // 关闭游标，以释放资源
                eventCursor.close()
            }
    }

    /**
     * 插入一个事件对象
     */
    fun insertCalendarEvent(calendarEventInfo: CalendarEventInfo) {
        insertCalendarEvent(
            eventTitle = calendarEventInfo.getEventTitle(),
            eventDescription = calendarEventInfo.getEventDescription(),
            eventLocation = calendarEventInfo.getEventLocation(),
            startTimeMillis = calendarEventInfo.getEventStartTimeMillis(),
            endTimeMillis = calendarEventInfo.getEventEndTimeMillis()
        )
    }

    /**
     * 插入系统日历事件
     *
     * calendarId：日历 ID
     * eventTitle：事件标题
     * eventDescription：事件描述
     * eventLocation：事件地点
     * startTimeMillis：开始的时间戳
     * endTimeMillis：结束的时间戳（如果是重复事件则没有结束时间，根据重复规则进行重复）
     */
    fun insertCalendarEvent(
        calendarId: Int = checkAndAddCalendarAccount(),
        eventTitle: String,
        eventDescription: String,
        eventLocation: String = "",
        startTimeMillis: Long,
        endTimeMillis: Long
    ) {
        Log.d(TAG, "insertCalendarEvent: ===>calendarId：$calendarId")
        if (calendarId == -1) {
            return
        }
        // 获取内容解析器
        val contentResolver = mContext.contentResolver
        // 执行添加日历事件的操作
        val event = contentValuesOf()
        event.run {
            // 日历 ID
            put(CalendarContract.Events.CALENDAR_ID, calendarId)
            // 事件的标题
            put(CalendarContract.Events.TITLE, eventTitle)
            // 事件的描述
            put(CalendarContract.Events.DESCRIPTION, eventDescription)
            // 事件的地点
            put(CalendarContract.Events.EVENT_LOCATION, eventLocation)
            // 事件开始时间
            put(CalendarContract.Events.DTSTART, startTimeMillis)
            if (isRepeat.not()) {
                // 一次性事件，设置结束时间
                put(CalendarContract.Events.DTEND, endTimeMillis)
            } else {
                // 重复事件，设置重复规则，默认设置为每天都重复
                put(CalendarContract.Events.RRULE, EVERY_DAY_RRLUE)
                // 事件的持续时间
                put(CalendarContract.Events.DURATION, "P60S")
            }
            // 设置事件是否有闹钟提醒，1：闹钟提醒
            put(CalendarContract.Events.HAS_ALARM, 1)
            // 设置事件的时区，必须设置
            put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai")
        }
        // 插入事件
        val insertEventUri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, event)
        // 解析插入事件的 ID
        val eventId = if (insertEventUri == null) -1 else ContentUris.parseId(insertEventUri)
        // 如果不是无效的事件 ID ，且需要提醒
        if (eventId != -1L && isAlarm) {
            if (ActivityCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.WRITE_CALENDAR
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.e(TAG, "addCalendarEvent: ===>没有日历写入权限！")
                return
            }
            // 开始组装事件提醒数据
            val reminders = ContentValues()
            // 此提醒所对应的事件ID
            reminders.put(CalendarContract.Reminders.EVENT_ID, eventId)
            // 设置提醒提前的时间(0：准时  -1：使用系统默认)
            reminders.put(CalendarContract.Reminders.MINUTES, mAlarmTime)
            // 设置事件提醒方式为通知警报
            reminders.put(
                CalendarContract.Reminders.METHOD,
                CalendarContract.Reminders.METHOD_ALERT
            )
            contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, reminders)
        }
        if (insertEventUri == null) {
            Log.e(TAG, "addCalendarEvent: ===>添加日程事件失败")
        }
    }

    /**
     * 根据事件标题删除日历事件（会删除所有同名的日历事件）
     */
    fun deleteEventByTitle(calendarEventInfo: CalendarEventInfo, block: () -> Unit) {
        deleteEventByTitle(calendarEventInfo.getEventTitle(), block)
    }

    /**
     * 根据事件标题删除日历事件（会删除所有同名的日历事件）
     */
    fun deleteEventByTitle(eventTitle: String, block: () -> Unit) {
        val contentResolver = mContext.contentResolver
        contentResolver
            .query(Uri.parse(CALENDER_EVENT_URL), null, null, null, null)
            .use { cursor ->
                cursor ?: return
                if (cursor.count > 0) {
                    // 遍历所有事件，找到 title 跟需要查询的 title 一样的项
                    cursor.moveToFirst()
                    while (cursor.isAfterLast.not()) {
                        val title =
                            cursor.getString(cursor.getColumnIndex("title"))
                        // 如果传入的事件标题不是空的，且事件标题和查询到的一致
                        if (eventTitle.isNotEmpty() && eventTitle == title) {
                            val id =
                                cursor.getInt(cursor.getColumnIndex(CalendarContract.Calendars._ID)) //取得id
                            // 获取日历事件的删除 Uri
                            val deleteEventUri = ContentUris.withAppendedId(
                                CalendarContract.Events.CONTENT_URI,
                                id.toLong()
                            )
                            // 根据 Uri 删除日历事件，并获得受影响的行数
                            val rows = contentResolver.delete(deleteEventUri, null, null)
                            // 如果受影响的行数为 -1 ，则事件删除失败了
                            if (rows == -1) { // 事件删除失败
                                return
                            }
                        }
                        // 将游标移动到下一行
                        cursor.moveToNext()
                    }
                    // 日历事件删除完毕的回调
                    block()
                }
                cursor.close()
            }
    }

    /**
     * 默认提供的日历事件信息接口的实现
     */
    class SimpleCalendarEvent(
        private val title: String,
        private val description: String = title,
        private val location: String = "",
        private val startTimeMillis: Long,
        private val endTimeMillis: Long = startTimeMillis + (AlarmManager.INTERVAL_FIFTEEN_MINUTES / 15)
    ) : CalendarEventInfo {

        override fun getEventTitle(): String = title

        override fun getEventDescription(): String = description

        override fun getEventLocation(): String = location

        override fun getEventStartTimeMillis(): Long = startTimeMillis

        override fun getEventEndTimeMillis(): Long = endTimeMillis
    }

    /**
     * 只要实现了该接口的对象都能用于系统日历事件的操作
     */
    interface CalendarEventInfo {
        fun getEventTitle(): String
        fun getEventDescription(): String
        fun getEventLocation(): String
        fun getEventStartTimeMillis(): Long
        fun getEventEndTimeMillis(): Long
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

        // 每天都提醒的日历事件重复规则
        private const val EVERY_DAY_RRLUE = "FREQ=DAILY;INTERVAL=1"
    }
}