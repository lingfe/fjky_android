package com.fjkyly.paradise.model
import com.google.gson.annotations.SerializedName

/**
 * 修改亲友信息
 */
data class ModifyFriendInfo(
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
        @SerializedName("and_relation")
        val andRelation: String,
        @SerializedName("contact_address")
        val contactAddress: Any,
        @SerializedName("crt_date")
        val crtDate: String,
        @SerializedName("crt_id")
        val crtId: Any,
        @SerializedName("full_name")
        val fullName: String,
        @SerializedName("id")
        val id: String,
        @SerializedName("id_card")
        val idCard: Any,
        @SerializedName("is_guardian")
        val isGuardian: Any,
        @SerializedName("list")
        val list: Any,
        @SerializedName("order_id")
        val orderId: Any,
        @SerializedName("original_id")
        val originalId: Any,
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