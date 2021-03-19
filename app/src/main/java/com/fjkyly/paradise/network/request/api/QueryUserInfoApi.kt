package com.fjkyly.paradise.network.request.api

import com.fjkyly.paradise.base.App
import com.fjkyly.paradise.model.QueryUserInfo
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface QueryUserInfoApi {

    /**
     * 获取用户信息
     */
    @FormUrlEncoded
    @POST("appUser/getUserInfo")
    fun query(
        @Field("user_id") userId: String = App.getUserId(),
        @Header("token") token: String = App.getUserToken()
    ): Call<QueryUserInfo>
}