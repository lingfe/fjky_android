package com.fjkyly.paradise.model
import com.google.gson.annotations.SerializedName

/**
 * 通用的数据返回实体
 */
data class HttpData(
    @SerializedName("msg")
    val msg: String,
    @SerializedName("state")
    val state: Int
)