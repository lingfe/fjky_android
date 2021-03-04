package com.fjkyly.paradise.network.request.api

import com.fjkyly.paradise.base.App
import com.fjkyly.paradise.model.ModifyUserName
import retrofit2.Call
import retrofit2.http.*

/**
 * 修改用户名
 */
interface ModifyUserNameApi {

    @FormUrlEncoded
    @POST("appUser/modifyUserName.app")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    fun modify(
        @Field("id") id: String = App.getUserId(), @Field("username") userName: String,
        @Header("token") token: String = App.getUserToken()
    ): Call<ModifyUserName>
}