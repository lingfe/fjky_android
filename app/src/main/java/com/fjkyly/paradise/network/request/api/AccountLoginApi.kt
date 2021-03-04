package com.fjkyly.paradise.network.request.api

import com.fjkyly.paradise.model.Login
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AccountLoginApi {

    /**
     * 登录账号
     *
     * @param accountNum String 账号（手机号/身份证号码）
     * @param accountPwd String 账号密码
     * @return Call<Login?>
     */
    @FormUrlEncoded
    @POST("appUser/appLogin")
    fun login(
        @Field("account_name") accountNum: String,
        @Field("account_pwd") accountPwd: String
    ): Call<Login>
}