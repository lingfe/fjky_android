package com.fjkyly.paradise.network.request.api

import com.fjkyly.paradise.base.App
import com.fjkyly.paradise.model.UserBasicInfo
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface QueryUserBasicInfoApi {

    /**
     * 获取用户相关数据(包含:用户名、头像、基本信息等)
     */
    @FormUrlEncoded
    @POST("appUser/getRelevantData")
    fun query(@Field("user_id") userId: String = App.getUserId()): Call<UserBasicInfo>
}