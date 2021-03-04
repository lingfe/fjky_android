package com.fjkyly.paradise.network.request

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.LifecycleOwner
import com.fjkyly.paradise.R
import com.hjq.http.EasyLog
import com.hjq.http.config.IRequestHandler
import com.hjq.http.exception.*
import okhttp3.Headers
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Type
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RequestHandler(private val application: Application) : IRequestHandler {

    @Throws(Exception::class)
    override fun requestSucceed(lifecycle: LifecycleOwner?, response: Response, type: Type): Any? {
        if (Response::class.java == type) {
            return response
        }
        if (!response.isSuccessful) {
            // 返回响应异常
            throw ResponseException(
                application.getString(R.string.http_response_error) + "，responseCode：" + response.code() + "，message：" + response.message(),
                response
            )
        }
        if (Headers::class.java == type) {
            return response.headers()
        }
        val body = response.body() ?: return null
        if (InputStream::class.java == type) {
            return body.byteStream()
        }
        val text: String = try {
            body.string()
        } catch (e: IOException) {
            // 返回结果读取异常
            throw DataException(application.getString(R.string.http_data_explain_error), e)
        }
        // 打印这个 Json 或者文本
        EasyLog.json(text)
        if (String::class.java == type) {
            return text
        }
        if (JSONObject::class.java == type) {
            return try {
                // 如果这是一个 JSONObject 对象
                JSONObject(text)
            } catch (e: JSONException) {
                throw DataException(application.getString(R.string.http_data_explain_error), e)
            }
        }
        if (JSONArray::class.java == type) {
            return try {
                // 如果这是一个 JSONArray 对象
                JSONArray(text)
            } catch (e: JSONException) {
                throw DataException(application.getString(R.string.http_data_explain_error), e)
            }
        }
        return null
    }

    override fun requestFail(lifecycle: LifecycleOwner?, e: Exception): Exception? {
        // 判断这个异常是不是自己抛的
        if (e is HttpException) {
            if (e is TokenException) {
                // 登录信息失效，跳转到登录页
            }
            return e
        }
        if (e is SocketTimeoutException) {
            return TimeoutException(application.getString(R.string.http_server_out_time), e)
        }
        if (e is UnknownHostException) {
            val info =
                (application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
            // 判断网络是否连接
            return if (info != null && info.isConnected) {
                // 有连接就是服务器的问题
                ServerException(application.getString(R.string.http_server_error), e)
            } else NetworkException(application.getString(R.string.http_network_error), e)
            // 没有连接就是网络异常
        }
        return if (e is IOException) {
            //e = new CancelException(context.getString(R.string.http_request_cancel), e);
            CancelException("", e)
        } else HttpException(e.message, e)
    }
}