package com.fjkyly.paradise.other

import android.os.Build
import androidx.annotation.RequiresApi

object WeekUtils {

    /**
     * 映射周一到周日的枚举类集合（操作系统日历时使用）
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private val mWeekMapping = mapOf(
        1 to Week.MONDAY,
        2 to Week.TUESDAY,
        3 to Week.WEDNESDAY,
        4 to Week.THURSDAY,
        5 to Week.FRIDAY,
        6 to Week.SATURDAY,
        7 to Week.SUNDAY
    )

    /**
     * 周一到周日分别对应：1-7
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getWeekByDayOfWeek(dayOfWeek: Int): Week? = mWeekMapping[dayOfWeek]

    /**
     * 星期的枚举类
     * abbreviation：星期几的缩写
     */
    enum class Week(abbreviation: String) {
        // Monday（星期一）
        MONDAY("MO"),

        // Tuesday（星期二）
        TUESDAY("TU"),

        // Wednesday（星期三）
        WEDNESDAY("WE"),

        // Thursday（星期四）
        THURSDAY("TH"),

        // Friday（星期五）
        FRIDAY("HR"),

        // Saturday（星期六）
        SATURDAY("SA"),

        // Sunday（星期天）
        SUNDAY("SU")
    }
}