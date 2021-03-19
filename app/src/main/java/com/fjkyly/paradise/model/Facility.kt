package com.fjkyly.paradise.model

import com.fjkyly.paradise.R

/**
 * 设备
 *
 * @property icon Int 设备图标
 * @property name String 设备名称
 * @property facilityBrandName String 设备品牌名称
 * @property facilityId String 设备ID
 * @property facilityType Int 设备类型
 * @property facilityStatus Int 设备状态 0、离线，1、在线
 * @property facilityBinding Boolean 设备是否已被绑定
 * @constructor
 */
data class Facility(
    val icon: Int = 0,
    val name: String,
    val facilityBrandName: String = "未知品牌$name",
    val facilityId: String,
    val facilityType: Int,
    var facilityStatus: Int = 0,
    val facilityTypeName: String = "未知设备类型",
    val facilityBinding: Boolean = false
) {
    fun getFacilityStatus(): String {
        return when (facilityStatus) {
            0 -> "设备连接失败"
            1 -> "在线"
            else -> "状态异常"
        }
    }

    fun getFacilityIcon() = when (facilityType) {
        0 -> R.drawable.icon_naozhong
        1 -> R.drawable.icon_yiyuan
        2 -> R.drawable.icon_yiyuan1
        3 -> R.drawable.icon_yiyuan2
        4 -> R.drawable.icon_youhuicuxiao
        5 -> R.drawable.icon_yujing
        else -> R.drawable.icon_anquan
    }
}
