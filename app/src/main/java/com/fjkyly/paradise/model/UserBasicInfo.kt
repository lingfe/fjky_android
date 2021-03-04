package com.fjkyly.paradise.model

import com.google.gson.annotations.SerializedName

/**
 * 用户基础信息
 */
data class UserBasicInfo(
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
        @SerializedName("crt_date")
        val crtDate: Long,
        @SerializedName("id")
        val id: String,
        @SerializedName("id_card")
        val idCard: String,
        @SerializedName("phone")
        val phone: String,
        @SerializedName("state")
        val state: Int,
        @SerializedName("yw_id")
        val ywId: String
    )
}