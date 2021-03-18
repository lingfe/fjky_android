package com.fjkyly.paradise.model

import com.google.gson.annotations.SerializedName

/**
 * 亲友列表
 */
data class FriendsList(
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
        @SerializedName("and_relation")
        val andRelation: String,
        @SerializedName("crt_date")
        val crtDate: Long,
        @SerializedName("crt_id")
        val crtId: String,
        @SerializedName("full_name")
        val fullName: String,
        @SerializedName("id")
        val id: String,
        @SerializedName("phone")
        val phone: String,
        @SerializedName("state")
        val state: Int,
        @SerializedName("yw_id")
        val ywId: String
    )
}