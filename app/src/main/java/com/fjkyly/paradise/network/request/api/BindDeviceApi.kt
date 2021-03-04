package com.fjkyly.paradise.network.request.api

import com.fjkyly.paradise.base.App
import com.fjkyly.paradise.model.BindDevice
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface BindDeviceApi {

    @FormUrlEncoded
    @POST("userDevice/userBindDevice.app")
    fun binding(
        @Field("user_id") userId: String = App.getUserId(),
        @Field("dev_id") deviceId: String, @Header("token") token: String = App.getUserToken()
    ): Call<BindDevice>
}