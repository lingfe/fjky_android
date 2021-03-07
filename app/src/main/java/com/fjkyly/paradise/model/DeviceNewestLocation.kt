package com.fjkyly.paradise.model
import com.google.gson.annotations.SerializedName

/**
 * 设备最新的一次位置
 */
 data class DeviceNewestLocation(
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
        @SerializedName("address")
        val address: String,
        @SerializedName("id")
        val id: String,
        @SerializedName("latitude")
        val latitude: String,
        @SerializedName("longitude")
        val longitude: String,
        @SerializedName("yw_id")
        val ywId: String
    )
}