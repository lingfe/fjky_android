package com.fjkyly.paradise.model

import com.google.gson.annotations.SerializedName

/**
 * 修改个人基本信息
 */
data class ModifyPersonalBasicInfoData(
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
        @SerializedName("balance")
        val balance: Any,
        @SerializedName("birthday")
        val birthday: Any,
        @SerializedName("crt_date")
        val crtDate: String,
        @SerializedName("crt_id")
        val crtId: Any,
        @SerializedName("diet_taboo")
        val dietTaboo: Any,
        @SerializedName("ess_bmi")
        val essBmi: Any,
        @SerializedName("ess_hipline")
        val essHipline: Any,
        @SerializedName("ess_stature")
        val essStature: Any,
        @SerializedName("ess_waistline")
        val essWaistline: Any,
        @SerializedName("ess_weight")
        val essWeight: Any,
        @SerializedName("ess_wh_than")
        val essWhThan: Any,
        @SerializedName("full_name")
        val fullName: String,
        @SerializedName("gender")
        val gender: Any,
        @SerializedName("hobby")
        val hobby: Any,
        @SerializedName("id")
        val id: String,
        @SerializedName("id_card")
        val idCard: Any,
        @SerializedName("img")
        val img: Any,
        @SerializedName("list")
        val list: Any,
        @SerializedName("nation")
        val nation: String,
        @SerializedName("order_id")
        val orderId: Any,
        @SerializedName("original_id")
        val originalId: Any,
        @SerializedName("permanent_address")
        val permanentAddress: Any,
        @SerializedName("phone")
        val phone: String,
        @SerializedName("reamrk")
        val reamrk: Any,
        @SerializedName("searchKey")
        val searchKey: Any,
        @SerializedName("state")
        val state: Int,
        @SerializedName("user_name")
        val userName: Any,
        @SerializedName("where")
        val `where`: Any,
        @SerializedName("yw_id")
        val ywId: Any
    )
}