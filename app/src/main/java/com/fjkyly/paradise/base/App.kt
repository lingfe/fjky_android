package com.fjkyly.paradise.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.blankj.utilcode.util.Utils
import com.bumptech.glide.Glide
import com.fjkyly.paradise.expand.ALERT_REQUEST_INTERVAL
import com.fjkyly.paradise.expand.AppConfig
import com.fjkyly.paradise.expand.REMIND_ALERT_URL
import com.fjkyly.paradise.expand.mainHandler
import com.fjkyly.paradise.model.Login
import com.fjkyly.paradise.network.request.Repository
import com.fjkyly.paradise.network.request.RequestHandler
import com.fjkyly.paradise.notification.notifyMention
import com.fjkyly.paradise.ui.activity.BrowserActivity
import com.fjkyly.paradise.worker.CalendarUpdateWorker
import com.hjq.http.EasyConfig
import com.hjq.http.ssl.HttpSslFactory
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.vondear.rxtool.RxTool
import okhttp3.OkHttpClient
import java.time.Duration

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
        init()
    }

    private fun init() {
        val sslConfig = HttpSslFactory.generateSslConfig()
        val okHttpClient = OkHttpClient.Builder()
            .sslSocketFactory(sslConfig.getsSLSocketFactory(), sslConfig.trustManager)
            .hostnameVerifier(HttpSslFactory.generateUnSafeHostnameVerifier())
            .build()
        EasyConfig.with(okHttpClient) // 是否打印日志
            .setLogEnabled(AppConfig.isDebug()) // 设置服务器配置
            .setServer("https://www.baidu.com/") // 设置请求处理策略
            .setHandler(RequestHandler(this)) // 设置请求重试次数
            .setRetryCount(3) // 添加全局请求参数
            //.addParam("token", "6666666")
            // 添加全局请求头
            //.addHeader("time", "20191030")
            // 启用配置
            .into()
        RxTool.init(this)
        Utils.init(this)
        initSDK()
        initTimerTask()
    }

    @SuppressLint("NewApi")
    private fun initTimerTask() {
        mainHandler.post(object : Runnable {
            override fun run() {
                if (getUserToken().isNotEmpty()) {
                    Repository.queryDeviceAlertListApi {
                        val data = it.data
                        if (data.isNotEmpty()) {
                            for (datum in data) {
                                val intent = Intent(this@App, BrowserActivity::class.java)
                                intent.putExtra("URL_LINK", REMIND_ALERT_URL)
                                notifyMention(
                                    context = appContext,
                                    id = datum.deviceId.toLongOrNull()?.toInt() ?: 0,
                                    title = datum.content,
                                    text = datum.content,
                                    intent = intent
                                )
                            }
                        }
                    }
                }
                mainHandler.postDelayed(this, ALERT_REQUEST_INTERVAL)
            }
        })
        // 设置任务的约束条件
        val constraints = Constraints.Builder()
            // 网络连接的时候才执行
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        // 每隔15分钟获取更新一次日历数据
        val periodicWorkRequest =
            PeriodicWorkRequest.Builder(CalendarUpdateWorker::class.java, Duration.ofMinutes(15))
                .setConstraints(constraints)
                .build()
        WorkManager.getInstance(this)
            .enqueue(periodicWorkRequest)
    }

    private fun initSDK() {
        preInitX5WebCore()
        //初始化X5浏览器内核环境
        QbSdk.initX5Environment(this, null)
        // 在调用TBS初始化、创建WebView之前进行如下配置
        val map = HashMap<String, Any>()
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(map)
    }

    private fun preInitX5WebCore() {
        if (!QbSdk.isTbsCoreInited()) {
            // 设置X5初始化完成的回调接口
            QbSdk.preInit(this, null)
        }
    }

    /**
     * 系统低内存时调用
     */
    override fun onLowMemory() {
        super.onLowMemory()
        // 清除 Glide 的图片缓存，以减少APP的内存占用
        Glide.get(this).onLowMemory()
    }

    companion object {

        private const val TAG = "App"

        @SuppressLint("StaticFieldLeak")
        @JvmStatic
        lateinit var appContext: Context

        var accountLoginInfo: Login? = null

        /**
         * 返回用户 token
         *
         * @return String
         */
        fun getUserToken(): String {
            var appToken = ""
            accountLoginInfo?.let {
                appToken = it.data.token
            }
            return appToken
        }

        /**
         * 返回用户 id
         *
         * @return String
         */
        fun getUserId(): String {
            var userId = ""
            accountLoginInfo?.let {
                userId = it.data.userInfo.id
            }
            return userId
        }

        /**
         * 获取用户名
         *
         * @return String
         */
        fun getUserName(): String {
            var userName = ""
            accountLoginInfo?.let {
                userName = it.data.userInfo.username
            }
            return userName
        }

        /**
         * 获取用户头像
         *
         * @return String
         */
        fun getUserAvatar(): String {
            var userAvatar = ""
            accountLoginInfo?.let {
                userAvatar = it.data.userInfo.userImg
            }
            return userAvatar
        }
    }
}