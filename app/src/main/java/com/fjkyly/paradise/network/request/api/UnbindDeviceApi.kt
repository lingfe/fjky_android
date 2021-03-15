package com.fjkyly.paradise.network.request.api

import com.fjkyly.paradise.base.App
import com.fjkyly.paradise.model.UnbindDevice
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface UnbindDeviceApi {

    /**
     * 解绑设备
     */
    @FormUrlEncoded
    @POST("userDevice/unbind.app")
    fun unbind(
        @Field("dev_id") deviceId: String,
        @Header("token") token: String = App.getUserToken()
    ): Call<UnbindDevice>
}