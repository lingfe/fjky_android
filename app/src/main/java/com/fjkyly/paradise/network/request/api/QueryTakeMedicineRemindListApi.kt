package com.fjkyly.paradise.network.request.api

import com.fjkyly.paradise.base.App
import com.fjkyly.paradise.model.TakeMedicineRemindList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface QueryTakeMedicineRemindListApi {

    @GET("mr/getMrList.app")
    fun query(@Header("token")token: String = App.getUserToken()): Call<TakeMedicineRemindList>
}