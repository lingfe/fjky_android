package com.fjkyly.paradise.network.request.api

import com.fjkyly.paradise.base.App
import com.fjkyly.paradise.model.DeviceNewestLocation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface QueryDeviceNewestLocationApi {

    /**
     * 查询设备最新的位置
     */
    @GET("address/getNewAddressInfo.app")
    fun query(@Header("token") token: String = App.getUserToken()): Call<DeviceNewestLocation>
}