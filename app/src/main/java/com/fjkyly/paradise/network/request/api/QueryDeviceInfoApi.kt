package com.fjkyly.paradise.network.request.api

import com.fjkyly.paradise.model.DeviceInfo
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * 通过设备 id 查询设备信息系
 */
interface QueryDeviceInfoApi {

    @FormUrlEncoded
    @POST("userDevice/wDeviceIdGetInfo")
    fun query(@Field("dev_id") deviceId: String): Call<DeviceInfo>
}