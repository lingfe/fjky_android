package com.fjkyly.paradise.network.request.api

import com.fjkyly.paradise.base.App
import com.fjkyly.paradise.model.UploadImage
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface UploadFileApi {

    /**
     * 上传图片文件
     */
    @Multipart
    @POST("images/imageUploadPublic")
    fun upload(
        @Query("yw_id") ywId: String = App.getUserId(),
        @Part multipartBodyPart: MultipartBody.Part,
    ): Call<UploadImage>
}