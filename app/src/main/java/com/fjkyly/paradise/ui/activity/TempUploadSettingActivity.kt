package com.fjkyly.paradise.ui.activity

import android.os.Bundle
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.databinding.ActivityTempUploadSettingBinding
import com.fjkyly.paradise.expand.simpleToast

class TempUploadSettingActivity : MyActivity() {

    private lateinit var mBinding: ActivityTempUploadSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityTempUploadSettingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initEvent() {
        mBinding.run {
            tempUploadSettingBackIv.setOnClickListener {
                finish()
            }
            tempUploadIntervalSaveBtn.setOnClickListener {
                simpleToast("体温上传间隔设置开发中...")
            }
        }
    }
}