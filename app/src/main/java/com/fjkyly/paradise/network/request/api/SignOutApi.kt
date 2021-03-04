package com.fjkyly.paradise.network.request.api

import com.fjkyly.paradise.base.App
import com.fjkyly.paradise.model.SignOut
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * 退出账号登录
 */
interface SignOutApi {

    @GET("appUser/quitLogin.app")
    fun singOut(@Header("token") token: String = App.getUserToken()): Call<SignOut>
}