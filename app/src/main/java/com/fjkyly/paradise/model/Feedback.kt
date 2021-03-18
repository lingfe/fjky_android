package com.fjkyly.paradise.model
import com.google.gson.annotations.SerializedName

/**
 * 意见反馈
 */
data class Feedback(
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
        @SerializedName("content")
        val content: String,
        @SerializedName("crt_date")
        val crtDate: String,
        @SerializedName("crt_id")
        val crtId: String,
        @SerializedName("id")
        val id: String,
        @SerializedName("list")
        val list: Any,
        @SerializedName("order_id")
        val orderId: Any,
        @SerializedName("original_id")
        val originalId: Any,
        @SerializedName("reamrk")
        val reamrk: Any,
        @SerializedName("searchKey")
        val searchKey: Any,
        @SerializedName("state")
        val state: Int,
        @SerializedName("user_name")
        val userName: Any,
        @SerializedName("where")
        val `where`: Any,
        @SerializedName("yw_id")
        val ywId: String
    )
}