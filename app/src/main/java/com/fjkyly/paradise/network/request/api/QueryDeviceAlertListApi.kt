package com.fjkyly.paradise.network.request.api

import com.fjkyly.paradise.base.App
import com.fjkyly.paradise.model.DeviceAlertingList
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface QueryDeviceAlertListApi {

    /**
     * 查询用户已绑定设备的告警信息
     */
    @FormUrlEncoded
    @POST("device_alerting/getDevAlertingList")
    fun query(@Field("user_id") userId: String = App.getUserId()): Call<DeviceAlertingList>
}