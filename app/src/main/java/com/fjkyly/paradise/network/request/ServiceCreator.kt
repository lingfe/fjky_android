package com.fjkyly.paradise.network.request

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {

    private const val BASE_URL = "http://192.168.124.25:8080/sys_fkcy/"
    // private const val BASE_URL = "http://47.106.198.137:82/sys_fkcy/"
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