package com.fjkyly.paradise.model

import com.google.gson.annotations.SerializedName

/**
 * 个人基本信息
 */
data class PersonBasicInfo(
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
        @SerializedName("age")
        val age: String,
        @SerializedName("birthday")
        val birthday: String,
        @SerializedName("crt_date")
        val crtDate: Long,
        @SerializedName("diet_taboo")
        val dietTaboo: String,
        @SerializedName("full_name")
        val fullName: String,
        @SerializedName("gender")
        val gender: String,
        @SerializedName("hobby")
        val hobby: String,
        @SerializedName("id")
        val id: String,
        @SerializedName("id_card")
        val idCard: String,
        @SerializedName("nation")
        val nation: String,
        @SerializedName("permanent_address")
        val permanentAddress: String,
        @SerializedName("phone")
        val phone: String,
        @SerializedName("state")
        val state: Int,
        @SerializedName("yw_id")
        val ywId: String,
        @SerializedName("img")
        val img: String
    )
}