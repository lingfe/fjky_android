package com.fjkyly.paradise.network.request.api

import com.fjkyly.paradise.base.App
import com.fjkyly.paradise.model.ModifyPersonalBasicInfoData
import com.fjkyly.paradise.model.PersonBasicInfo
import retrofit2.Call
import retrofit2.http.*

/**
 * 个人基本信息的设置与获取
 */
interface PersonBasicInfoApi {

    /**
     * 获取个人基本信息
     */
    @FormUrlEncoded
    @POST("appUser/getEssInfo.app")
    fun queryPersonBasicInfo(
        @Field("appUserId") userId: String = App.getUserId(),
        @Header("token") token: String = App.getUserToken()
    ): Call<PersonBasicInfo>

    /**
     * 设置个人基本信息
     */
    @FormUrlEncoded
    @POST("appUser/setEssInfo.app")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    fun modifyPersonBasicInfo(
        @Field("appUserId") userId: String = App.getUserId(),
        @FieldMap params: Map<String, String>,
        @Header("token") token: String = App.getUserToken()
    ): Call<ModifyPersonalBasicInfoData>
}