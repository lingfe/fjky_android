package com.fjkyly.paradise.network.request.api

import com.fjkyly.paradise.base.App
import com.fjkyly.paradise.model.Feedback
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * 意见反馈
 */
interface FeedbackApi {

    @FormUrlEncoded
    @POST("feedback/save.app")
    fun feedback(
        @Field("content") content: String,
        @Header("token") token: String = App.getUserToken()
    ): Call<Feedback>
}