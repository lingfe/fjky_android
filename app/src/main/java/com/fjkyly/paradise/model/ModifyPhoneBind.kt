package com.fjkyly.paradise.model
import com.google.gson.annotations.SerializedName

/**
 * 用户更换手机号码绑定
 */
data class ModifyPhoneBind(
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
        @SerializedName("balance")
        val balance: Double,
        @SerializedName("crt_date")
        val crtDate: String,
        @SerializedName("crt_id")
        val crtId: Any,
        @SerializedName("id")
        val id: String,
        @SerializedName("list")
        val list: Any,
        @SerializedName("order_id")
        val orderId: Any,
        @SerializedName("original_id")
        val originalId: Any,
        @SerializedName("phone")
        val phone: String,
        @SerializedName("pwd")
        val pwd: Any,
        @SerializedName("reamrk")
        val reamrk: Any,
        @SerializedName("searchKey")
        val searchKey: Any,
        @SerializedName("state")
        val state: Int,
        @SerializedName("user_img")
        val userImg: Any,
        @SerializedName("user_name")
        val userName: Any,
        @SerializedName("username")
        val username: Any,
        @SerializedName("where")
        val `where`: Any
    )
}