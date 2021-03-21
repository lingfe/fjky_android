package com.fjkyly.paradise.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.blankj.utilcode.util.GsonUtils
import com.fjkyly.paradise.network.request.Repository
import com.fjkyly.paradise.other.CalendarUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * 日历更新的任务
 */
class CalendarUpdateWorker(val context: Context, params: WorkerParameters) :
    Worker(context, params) {

    override fun doWork(): Result {
        Repository.queryTakeMedicineRemindList(lifecycle = null) {
            val data = it.data
            for (datum in data) {
                runCatching {
                    CalendarUtils().deleteEventByTitle(datum.mrTitle) {}
                }
            }
            for (datum in data) {
                runCatching {
                    val startDate = SimpleDateFormat(
                        "yyyy.MM.dd-HH:mm",
                        Locale.getDefault()
                    ).parse(datum.mrStartDate + "-" + datum.mrTime)
                    val endDate = SimpleDateFormat(
                        "yyyy.MM.dd-HH:mm",
                        Locale.getDefault()
                    ).parse(datum.mrEntDate + "-" + datum.mrTime)
                    if (startDate != null && endDate != null) {
                        val calendarEvent = CalendarUtils.SimpleCalendarEvent(
                            title = datum.mrTitle,
                            description = datum.mrTitle,
                            location = "",
                            startTimeMillis = startDate.time,
                            endTimeMillis = endDate.time
                        )
                        runCatching {
                            CalendarUtils()
                                .setRepeat(false)
                                .insertCalendarEvent(calendarEventInfo = calendarEvent)
                        }
                    }
                }
            }
            Log.d(TAG, "doWork: ===>TakeMedicineRemindList：${GsonUtils.toJson(it)}")
        }
        return Result.success()
    }

    companion object {
        private const val TAG = "CalendarUpdateWorker"
    }
}