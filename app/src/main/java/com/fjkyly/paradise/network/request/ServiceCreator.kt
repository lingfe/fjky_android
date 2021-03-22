package com.fjkyly.paradise.network.request

import android.util.Log
import com.fjkyly.paradise.expand.AppConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration

object ServiceCreator {

    private const val OFF_LINE_BASE_URL = "http://192.168.124.25:8080/sys_fkcy/"
    private const val ON_LINE_BASE_URL = "http://47.106.198.137:82/sys_fkcy/"
    private val BASE_URL = if (AppConfig.isDebug()) OFF_LINE_BASE_URL else ON_LINE_BASE_URL
    private val client = OkHttpClient()
        .newBuilder()
        .readTimeout(Duration.ofMinutes(3))
        .connectTimeout(Duration.ofMinutes(3))
        .writeTimeout(Duration.ofMinutes(3))
        .addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Log.d(TAG, "ServiceCreator: ===>$it")
        }))
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)

    private const val TAG = "ServiceCreator"
}