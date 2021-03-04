package com.fjkyly.paradise.model
import com.google.gson.annotations.SerializedName

/**
 * 退出登录
 */
data class SignOut(
    @SerializedName("code")
    val code: Int,
    @SerializedName("count")
    val count: Int,
    @SerializedName("data")
    val `data`: Any,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("state")
    val state: Int
)