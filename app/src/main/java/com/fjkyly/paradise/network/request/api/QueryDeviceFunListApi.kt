package com.fjkyly.paradise.network.request.api

import com.fjkyly.paradise.model.DeviceSettingFun
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface QueryDeviceFunListApi {

    /**
     * 查询设备的功能列表
     */
    @FormUrlEncoded
    @POST("device_function/getDevFunctionList")
    fun query(@Field("dev_id") deviceId: String): Call<DeviceSettingFun>
}