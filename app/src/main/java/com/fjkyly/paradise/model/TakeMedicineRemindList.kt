package com.fjkyly.paradise.model
import com.google.gson.annotations.SerializedName

/**
 * 吃药提醒列表
 */
data class TakeMedicineRemindList(
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
        @SerializedName("crt_date")
        val crtDate: Long,
        @SerializedName("crt_id")
        val crtId: String,
        @SerializedName("id")
        val id: String,
        @SerializedName("mr_ent_date")
        val mrEntDate: String,
        @SerializedName("mr_start_date")
        val mrStartDate: String,
        @SerializedName("mr_time")
        val mrTime: String,
        @SerializedName("mr_title")
        val mrTitle: String,
        @SerializedName("mr_txt")
        val mrTxt: String,
        @SerializedName("mr_way")
        val mrWay: Int,
        @SerializedName("state")
        val state: Int,
        @SerializedName("yw_id")
        val ywId: String
    )
}