package com.fjkyly.paradise.network.request.api

import com.fjkyly.paradise.model.Register
import retrofit2.Call
import retrofit2.http.*

interface AccountRegisterApi {

    /**
     * 注册账号
     *
     * @param phoneNum String 手机号码
     * @param pwd String 账号密码
     * @param idCard String 身份证号码
     * @return Call<Register?>
     */
    @FormUrlEncoded
    @POST("appUser/appRegister")
    fun register(
        @Field("phone") phoneNum: String,
        @Field("pwd") pwd: String,
        @Field("id_card") idCard: String
    ): Call<Register>
}