package com.fjkyly.paradise.model

/**
 * 设备
 *
 * @property icon Int 设备图标
 * @property name String 设备名称
 * @property facilityId String 设备ID
 * @property facilityType Int 设备类型
 * @property facilityStatus Int 设备状态 0、离线，1、在线
 * @constructor
 */
data class Facility(
    val icon: Int = 0,
    val name: String,
    val facilityId: String,
    val facilityType: Int,
    var facilityStatus: Int = 0
) {
    fun getFacilityStatus(): String {
        return when (facilityStatus) {
            0 -> "离线"
            1 -> "在线"
            else -> "状态异常"
        }
    }
}
