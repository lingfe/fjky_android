package com.fjkyly.paradise.ui.fragment

import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import com.fjkyly.paradise.R
import com.fjkyly.paradise.base.App
import com.fjkyly.paradise.base.BaseFragment
import com.fjkyly.paradise.databinding.FragmentBrowserBinding
import com.fjkyly.paradise.expand.simpleToast
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebViewClient

/**
 * 浏览器界面
 *
 * @property _binding FragmentBrowserBinding?
 * @property mBinding FragmentBrowserBinding
 */
class BrowserFragment : BaseFragment() {

    private var _binding: FragmentBrowserBinding? = null
    private val mBinding get() = _binding!!

    override fun getLayoutResId(): Int = R.layout.fragment_browser

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentBrowserBinding.bind(view)
        callAllInit()
    }

    override fun initView() {
        mBinding.run {
            x5WebView.run {
                // loadUrl("${ASSETS_FOLDER}h5/index.html")
                webViewClient = WebViewClient()
                webChromeClient = WebChromeClient()
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
                loadUrl("http://47.106.198.137:82/#/")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding.run {
            x5WebView.removeJavascriptInterface("appNative")
        }
        _binding = null
    }

    inner class AppNative {

        /**
         * 获取用户登录的 Token 信息
         *
         * @return String?
         */
        @JavascriptInterface
        fun getUserToken(): String  = App.getUserToken()

        @JavascriptInterface
        fun toast() {
            simpleToast("调用了安卓原生方法")
        }
    }

    companion object {
        const val ASSETS_FOLDER = "file:///android_asset/"
    }
}