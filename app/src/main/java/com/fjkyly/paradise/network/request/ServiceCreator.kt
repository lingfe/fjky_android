package com.fjkyly.paradise.network.request

import com.fjkyly.paradise.expand.AppConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {

    private const val OFF_LINE_BASE_URL = "http://192.168.124.25:8080/sys_fkcy/"
    private const val ON_LINE_BASE_URL = "http://47.106.198.137:82/sys_fkcy/"
    private val BASE_URL = if (AppConfig.isDebug()) OFF_LINE_BASE_URL else ON_LINE_BASE_URL
    private val client = OkHttpClient()
        .newBuilder()
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)
}