package com.fjkyly.paradise.expand

import java.util.*

/**
 * 根据出生日期自动计算年龄
 */
fun getAgeByBirth(birthDay: Date): Int {
    val calendar = Calendar.getInstance()
    if (calendar.before(birthDay)) return 0 // 出生日期晚于当前时间，无法计算
    val nowYear = calendar.get(Calendar.YEAR) // 当前年份
    val nowMonth = calendar.get(Calendar.MONTH) // 当前月份
    val nowDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH) // 当前日期
    calendar.time = birthDay
    val yearBirth = calendar.get(Calendar.YEAR)
    val monthBirth = calendar.get(Calendar.MONTH)
    val dayOfMonthBirth = calendar.get(Calendar.DAY_OF_MONTH)
    var age = nowYear - yearBirth // 计算整岁数
    if (nowMonth <= monthBirth) {
        if (nowMonth == monthBirth) {
            if (nowDayOfMonth < dayOfMonthBirth) age-- //当前日期在生日之前，年龄减一
        } else {
            age-- // 当前月份在生日之前，年龄减一
        }
    }
    return if (age < 0) 0 else age
}