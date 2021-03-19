package com.fjkyly.paradise.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.format.Formatter
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.CacheDiskStaticUtils
import com.bumptech.glide.Glide
import com.fjkyly.paradise.base.App
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.databinding.ActivityAppSettingBinding
import com.fjkyly.paradise.expand.AppConfig
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.expand.startActivity
import com.vondear.rxtool.RxFileTool
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * APP设置界面
 */
class AppSettingActivity : MyActivity() {

    private lateinit var mBinding: ActivityAppSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAppSettingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        mBinding.run {
            appSettingCurrentVersionTv.text = "当前版本：V${AppConfig.getVersionName()}"
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        mBinding.run {
            val cacheSize = CacheDiskStaticUtils.getCacheSize()
            appSettingCacheTv.text = Formatter.formatFileSize(this@AppSettingActivity, cacheSize)
        }
    }

    override fun initEvent() {
        mBinding.run {
            accountMangerBackIv.setOnClickListener {
                finish()
            }
            appSettingClearCacheContainer.setOnClickListener {
                // 清空外部缓存
                lifecycleScope.launch {
                    val isOk = withContext(Dispatchers.IO) {
                        Glide.get(App.appContext).clearDiskCache()
                        RxFileTool.cleanExternalCache(this@AppSettingActivity)
                        true
                    }
                    if (isOk) {
                        loadData()
                        simpleToast("缓存清理完毕")
                    }
                }
            }
            appSettingUserAgreementContainer.setOnClickListener {
                // 跳转到用户协议界面
                startActivity<UserAgreementActivity>()
            }
        }
    }
}