package com.fjkyly.paradise.model

import com.google.gson.annotations.SerializedName

/**
 * 用户基础信息（包含:用户名、头像、基本信息等）
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
        @SerializedName("acc_list")
        val accList: List<Acc>,
        @SerializedName("ess_info")
        val essInfo: EssInfo,
        @SerializedName("style_info")
        val styleInfo: StyleInfo,
        @SerializedName("user_info")
        val userInfo: UserInfo
    ) {
        data class Acc(
            @SerializedName("account_name")
            val accountName: String,
            @SerializedName("account_pwd")
            val accountPwd: String,
            @SerializedName("crt_date")
            val crtDate: Long,
            @SerializedName("id")
            val id: String,
            @SerializedName("state")
            val state: Int,
            @SerializedName("yw_id")
            val ywId: String
        )

        data class EssInfo(
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

        data class StyleInfo(
            @SerializedName("crt_date")
            val crtDate: Long,
            @SerializedName("id")
            val id: String,
            @SerializedName("state")
            val state: Int,
            @SerializedName("yw_id")
            val ywId: String
        )

        data class UserInfo(
            @SerializedName("crt_date")
            val crtDate: Long,
            @SerializedName("id")
            val id: String,
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
}