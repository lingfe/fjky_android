package com.fjkyly.paradise.model
import com.google.gson.annotations.SerializedName

/**
 * 绑定的设备列表
 */
data class BindDeviceList(
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
        @SerializedName("crt_date")
        val crtDate: Long,
        @SerializedName("crt_id")
        val crtId: String,
        @SerializedName("dexplain")
        val dexplain: String,
        @SerializedName("dmodel")
        val dmodel: String,
        @SerializedName("dname")
        val dname: String,
        @SerializedName("dtype_code")
        val dtypeCode: Int,
        @SerializedName("dtype_id")
        val dtypeId: String,
        @SerializedName("dtype_name")
        val dtypeName: String,
        @SerializedName("id")
        val id: String,
        @SerializedName("imei")
        val imei: String,
        @SerializedName("sort")
        val sort: Int,
        @SerializedName("state")
        val state: Int,
        @SerializedName("supp_id")
        val suppId: String,
        @SerializedName("supp_name")
        val suppName: String,
        @SerializedName("user_id")
        val userId: String,
        @SerializedName("user_name")
        val userName: String,
        @SerializedName("yw_id")
        val ywId: String
    )
}