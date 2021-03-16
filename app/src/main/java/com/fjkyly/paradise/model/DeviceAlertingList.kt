package com.fjkyly.paradise.model

import com.google.gson.annotations.SerializedName

/**
 * 设备告警信息列表
 */
data class DeviceAlertingList(
    @SerializedName("code")
    val code: Int,
    @SerializedName("count")
    val count: Int,
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("state")
    val state: Int
) {
    data class Data(
        @SerializedName("content")
        val content: String,
        @SerializedName("crt_date")
        val crtDate: Long,
        @SerializedName("device_id")
        val deviceId: String,
        @SerializedName("id")
        val id: String,
        @SerializedName("state")
        val state: Int,
        @SerializedName("yw_id")
        val ywId: String
    )
}