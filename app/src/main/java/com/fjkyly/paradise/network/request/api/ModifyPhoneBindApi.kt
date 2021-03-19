package com.fjkyly.paradise.network.request.api

import com.fjkyly.paradise.base.App
import com.fjkyly.paradise.model.ModifyPhoneBind
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface ModifyPhoneBindApi {

    /**
     * 更换手机号绑定
     */
    @FormUrlEncoded
    @POST("appUser/changePhone.app")
    fun modifyPhoneBind(
        @Field("new_phone") newPhone: String,
        @Header("token") token: String = App.getUserToken()
    ): Call<ModifyPhoneBind>
}