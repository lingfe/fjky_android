package com.fjkyly.paradise.model
import com.google.gson.annotations.SerializedName

/**
 * 修改用户头像
 *
 * @property code Int
 * @property count Int
 * @property `data` Any
 * @property msg String
 * @property state Int
 * @constructor
 */
data class ModifyUserAvatar(
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