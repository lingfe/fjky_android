package com.fjkyly.paradise.model
import com.google.gson.annotations.SerializedName

/**
 * 解绑设备
 */
 data class UnbindDevice(
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
        val crtDate: String,
        @SerializedName("crt_id")
        val crtId: Any,
        @SerializedName("dexplain")
        val dexplain: Any,
        @SerializedName("dicon")
        val dicon: Any,
        @SerializedName("dmodel")
        val dmodel: Any,
        @SerializedName("dname")
        val dname: Any,
        @SerializedName("dtype_id")
        val dtypeId: Any,
        @SerializedName("dtype_name")
        val dtypeName: Any,
        @SerializedName("id")
        val id: String,
        @SerializedName("imei")
        val imei: Any,
        @SerializedName("list")
        val list: Any,
        @SerializedName("order_id")
        val orderId: Any,
        @SerializedName("original_id")
        val originalId: Any,
        @SerializedName("reamrk")
        val reamrk: Any,
        @SerializedName("searchKey")
        val searchKey: Any,
        @SerializedName("state")
        val state: Int,
        @SerializedName("supp_id")
        val suppId: Any,
        @SerializedName("supp_name")
        val suppName: Any,
        @SerializedName("user_id")
        val userId: String,
        @SerializedName("user_name")
        val userName: String,
        @SerializedName("where")
        val `where`: Any,
        @SerializedName("yw_id")
        val ywId: Any
    )
}