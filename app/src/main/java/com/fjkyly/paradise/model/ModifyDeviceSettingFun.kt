package com.fjkyly.paradise.model
import com.google.gson.annotations.SerializedName

/**
 * 修改设备功能参数
 */
data class ModifyDeviceSettingFun(
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
        @SerializedName("data")
        val `data`: String
    )
}