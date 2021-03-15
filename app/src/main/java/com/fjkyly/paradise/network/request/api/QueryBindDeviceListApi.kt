package com.fjkyly.paradise.network.request.api

import com.fjkyly.paradise.base.App
import com.fjkyly.paradise.model.BindDeviceList
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface QueryBindDeviceListApi {

    /**
     * 查询绑定的设备列表
     */
    @FormUrlEncoded
    @POST("userDevice/getBindDeviceList.app")
    fun query(
        @Field("user_id") userId: String = App.getUserId(),
        @Header("token") token: String = App.getUserToken()
    ): Call<BindDeviceList>
}