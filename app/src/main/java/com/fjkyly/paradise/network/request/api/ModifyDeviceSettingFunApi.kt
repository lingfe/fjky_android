package com.fjkyly.paradise.network.request.api

import com.fjkyly.paradise.model.ModifyDeviceSettingFun
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ModifyDeviceSettingFunApi {

    /**
     * 修改设备功能参数
     */
    @FormUrlEncoded
    @POST("device_function/setDevCode")
    fun modify(
        @Field("dev_fun_id") deviceFunId: String,
        @Field("code_value") deviceFunValue: String
    ): Call<ModifyDeviceSettingFun>
}