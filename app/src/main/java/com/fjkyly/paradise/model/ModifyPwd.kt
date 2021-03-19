package com.fjkyly.paradise.model
import com.google.gson.annotations.SerializedName

/**
 * 修改密码
 */
data class ModifyPwd(
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