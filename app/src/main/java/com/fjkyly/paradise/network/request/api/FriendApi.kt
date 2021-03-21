package com.fjkyly.paradise.network.request.api

import com.fjkyly.paradise.base.App
import com.fjkyly.paradise.model.AddFriend
import com.fjkyly.paradise.model.FriendsList
import com.fjkyly.paradise.model.HttpData
import com.fjkyly.paradise.model.ModifyFriendInfo
import retrofit2.Call
import retrofit2.http.*

/**
 * 亲友
 */
interface FriendApi {

    /**
     * 根据亲友 ID 删除亲友
     */
    @FormUrlEncoded
    @POST("raf/deleteWhereId.app")
    fun deleteFriendById(
        @Field("id") friendId: String,
        @Header("token") token: String = App.getUserToken()): Call<HttpData>

    /**
     * 修改亲友信息
     */
    @FormUrlEncoded
    @POST("raf/modifyRAF.app")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    fun modifyFriendInfo(
        @Field("appUserId") userId: String = App.getUserId(),
        @Field("id") friendId: String,
        @Field("full_name") friendName: String,
        @Field("phone") friendPhoneNum: String,
        @Field("and_relation") friendRelation: String,
        @Header("token") token: String = App.getUserToken()
    ): Call<ModifyFriendInfo>

    /**
     * 获取亲友列表
     */
    @FormUrlEncoded
    @POST("raf/getRAF.app")
    fun queryFriendsList(
        @Field("appUserId") userId: String = App.getUserId(),
        @Header("token") token: String = App.getUserToken()
    ): Call<FriendsList>

    /**
     * 添加亲友
     */
    @FormUrlEncoded
    @POST("raf/add.app")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    fun addFriend(
        @Field("appUserId") userId: String = App.getUserId(),
        @Field("full_name") friendName: String,
        @Field("phone") friendPhoneNum: String,
        @Field("and_relation") friendRelation: String,
        @Header("token") token: String = App.getUserToken()
    ): Call<AddFriend>
}