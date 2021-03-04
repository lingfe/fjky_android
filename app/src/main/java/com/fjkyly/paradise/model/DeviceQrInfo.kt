package com.fjkyly.paradise.model
import com.google.gson.annotations.SerializedName


/**
 * 设备识别的二维码信息
 */
data class DeviceQrInfo(
    @SerializedName("code")
    val code: Int,
    @SerializedName("count")
    val count: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("state")
    val state: Int
) {
    data class Data(
        @SerializedName("dev_id")
        val devId: String
    )
}