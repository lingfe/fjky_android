package com.fjkyly.paradise.ui.fragment

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import androidx.lifecycle.lifecycleScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.blankj.utilcode.util.GsonUtils
import com.fjkyly.paradise.R
import com.fjkyly.paradise.base.App
import com.fjkyly.paradise.base.BaseFragment
import com.fjkyly.paradise.databinding.FragmentBrowserBinding
import com.fjkyly.paradise.expand.fromJson
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.other.CalendarUtils
import com.fjkyly.paradise.provider.LocationProvider
import com.fjkyly.paradise.worker.CalendarUpdateWorker
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.tencent.smtt.export.external.interfaces.ConsoleMessage
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebViewClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 浏览器界面
 *
 * @property _binding FragmentBrowserBinding?
 * @property mBinding FragmentBrowserBinding
 */
class BrowserFragment(private var mUrl: String = DEFAULT_URL) : BaseFragment() {

    private var _binding: FragmentBrowserBinding? = null
    private val mBinding get() = _binding!!

    override fun getLayoutResId(): Int = R.layout.fragment_browser

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentBrowserBinding.bind(view)
        callAllInit()
    }

    fun setUrl(urlLink: String): BrowserFragment {
        mUrl = urlLink
        return this
    }

    override fun initView() {
        mBinding.run {
            x5WebView.run {
                // loadUrl("${ASSETS_FOLDER}h5/index.html")
                webViewClient = WebViewClient()
                webChromeClient = object : WebChromeClient() {
                    override fun onConsoleMessage(p0: ConsoleMessage?): Boolean {
                        Log.d(TAG, "onConsoleMessage: ===>ConsoleMessage：${GsonUtils.toJson(p0)}")
                        return super.onConsoleMessage(p0)
                    }
                }
                // 注入一个APP本地对象
                addJavascriptInterface(AppNative(), "appNative")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mBinding.run {
            x5WebView.run {
                // loadUrl("http://192.168.124.17:81/")
                // loadUrl("http://47.106.198.137:82/#/")
                loadUrl(mUrl)
                // loadUrl(ASSETS_FOLDER + "h5/getLocation.html")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.run {
            x5WebView.removeJavascriptInterface("appNative")
        }
        _binding = null
    }

    inner class AppNative {

        /**
         * 是否获得日历读写权限
         */
        private var obtainCalendarPermission = false

        /**
         * 获取用户登录的 Token 信息
         *
         * @return String
         */
        @JavascriptInterface
        fun getUserToken(): String = App.getUserToken()

        /**
         * 获取用户 id
         */
        @JavascriptInterface
        fun getUserId(): String = App.getUserId()

        /**
         * 获取用户的位置信息，包含经度和纬度的Json字符串，
         * 需要前端获取后进行解析
         */
        @JavascriptInterface
        fun getLocationJson(): String =
            LocationProvider.getLocationJson(this@BrowserFragment.requireContext())

        @JavascriptInterface
        fun toast() {
            simpleToast("调用了安卓原生方法")
        }

        /**
         * 操作日历之前先检查权限是否已经获取
         */
        @JavascriptInterface
        fun checkCalendarPermission() {
            lifecycleScope.launch {
                val result = withContext(Dispatchers.Default) {
                    XXPermissions.with(this@BrowserFragment)
                        .permission(
                            arrayOf(
                                Manifest.permission.WRITE_CALENDAR,
                                Manifest.permission.READ_CALENDAR
                            )
                        )
                        .request(object : OnPermissionCallback {
                            override fun onGranted(
                                permissions: MutableList<String>?,
                                all: Boolean
                            ) {
                                obtainCalendarPermission = all
                            }

                            override fun onDenied(
                                permissions: MutableList<String>?,
                                never: Boolean
                            ) {
                                obtainCalendarPermission = never.not()
                            }
                        })
                }
            }
        }

        /**
         * 返回是否已经获取到日历读写权限
         */
        @JavascriptInterface
        fun hasCalendarPermission() = obtainCalendarPermission

        @JavascriptInterface
        fun insertCalendarEvent(eventJson: String) {
            val calendarEvent: CalendarUtils.SimpleCalendarEvent = fromJson(eventJson)
            CalendarUtils().insertCalendarEvent(calendarEventInfo = calendarEvent)
        }

        /**
         * 通知更新日历数据，从服务器获取最新的吃药提醒数据并插入到系统日历
         */
        @JavascriptInterface
        fun updateCalendarEvent() {
            val workerRequest = OneTimeWorkRequestBuilder<CalendarUpdateWorker>()
                .build()
            WorkManager.getInstance(this@BrowserFragment.requireContext())
                .enqueue(workerRequest)
        }
    }

    companion object {
        private const val TAG = "BrowserFragment"
        const val ASSETS_FOLDER = "file:///android_asset/"
        const val DEFAULT_URL = "http://47.106.198.137:82/#/"
    }
}