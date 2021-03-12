package com.fjkyly.paradise.model
import com.google.gson.annotations.SerializedName

/**
 * 设备设置界面的菜单属性
 */
 data class DeviceSettingFun(
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
        @SerializedName("crt_id")
        val crtId: String,
        @SerializedName("dev_fun_list")
        val devFunList: List<DevFun>,
        @SerializedName("dmodel")
        val dmodel: String,
        @SerializedName("dname")
        val dname: String,
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
    ) {
        data class DevFun(
            @SerializedName("api_id")
            val apiId: String,
            @SerializedName("api_name")
            val apiName: String,
            @SerializedName("crt_date")
            val crtDate: Long,
            @SerializedName("device_id")
            val deviceId: String,
            @SerializedName("device_name")
            val deviceName: String,
            @SerializedName("fun_code")
            val funCode: String,
            @SerializedName("fun_id")
            val funId: String,
            @SerializedName("fun_name")
            val funName: String,
            @SerializedName("fun_value")
            val funValue: String,
            @SerializedName("id")
            val id: String,
            @SerializedName("state")
            val state: Int
        )
    }
}