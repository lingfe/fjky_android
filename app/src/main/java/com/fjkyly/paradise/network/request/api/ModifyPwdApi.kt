package com.fjkyly.paradise.network.request.api

import com.fjkyly.paradise.base.App
import com.fjkyly.paradise.model.ModifyPwd
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface ModifyPwdApi {

    @FormUrlEncoded
    @POST("appUser/modifyPwd.app")
    fun modify(
        @Field("phone") phone: String,
        @Field("pwd") oldPwd: String,
        @Field("new_pwd") newPwd: String,
        @Header("token") token: String = App.getUserToken()
    ): Call<ModifyPwd>
}