package com.fjkyly.paradise.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import com.blankj.utilcode.util.GsonUtils
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.databinding.ActivityUserAgreementBinding
import com.fjkyly.paradise.ui.fragment.BrowserFragment
import com.tencent.smtt.export.external.interfaces.ConsoleMessage
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebViewClient

class UserAgreementActivity : MyActivity() {

    private var _binding: ActivityUserAgreementBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUserAgreementBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    @SuppressLint("WrongConstant")
    override fun initView() {
        mBinding.run {
            x5WebView.run {
                // 不支持缩放
                settings.setSupportZoom(false)
                loadUrl("${BrowserFragment.ASSETS_FOLDER}h5/appUserAgreement.html")
                webViewClient = WebViewClient()
                webChromeClient = object : WebChromeClient() {
                    override fun onConsoleMessage(p0: ConsoleMessage?): Boolean {
                        Log.d(TAG, "onConsoleMessage: ===>ConsoleMessage：${GsonUtils.toJson(p0)}")
                        return super.onConsoleMessage(p0)
                    }
                }
            }
        }
    }

    override fun initEvent() {
        mBinding.run {
            userAgreementBackIv.setOnClickListener {
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "UserAgreementActivity"
    }
}