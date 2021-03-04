package com.fjkyly.paradise.network.request.api

import com.fjkyly.paradise.base.App
import com.fjkyly.paradise.model.UploadImage
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface UploadFileApi {

    @Multipart
    @POST("images/imageUploadPublic")
    fun upload(
        @Query("yw_id") ywId: String = App.getUserId(),
        @Query("folder") folder: String = App.getUserId(),
        @Part multipartBodyPart: MultipartBody.Part
    ): Call<UploadImage>
}