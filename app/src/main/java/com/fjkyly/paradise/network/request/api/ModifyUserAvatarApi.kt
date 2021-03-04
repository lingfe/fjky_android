package com.fjkyly.paradise.network.request.api

import com.fjkyly.paradise.base.App
import com.fjkyly.paradise.model.ModifyUserAvatar
import retrofit2.Call
import retrofit2.http.*

/**
 * 修改用户头像
 */
interface ModifyUserAvatarApi {

    @FormUrlEncoded
    @POST("appUser/modifyHeadImg.app")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    fun modify(
        @Field("id") id: String = App.getUserId(), @Field("user_img") newUserAvatar: String,
        @Header("token") token: String = App.getUserToken()
    ): Call<ModifyUserAvatar>
}