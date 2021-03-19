package com.fjkyly.paradise.model

import com.google.gson.annotations.SerializedName

/**
 * 获取用户信息
 */
data class QueryUserInfo(
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
        val crtDate: Long,
        @SerializedName("id")
        val id: String,
        @SerializedName("phone")
        val phone: String,
        @SerializedName("pwd")
        val pwd: String,
        @SerializedName("state")
        val state: Int,
        @SerializedName("user_img")
        val userImg: String,
        @SerializedName("username")
        val username: String
    )
}