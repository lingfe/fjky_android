package com.fjkyly.paradise.ui.activity

import android.os.Bundle
import com.fjkyly.paradise.base.BaseActivity
import com.fjkyly.paradise.databinding.ActivityFeedbackSettingBinding
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.network.request.Repository

class FeedbackSettingActivity : BaseActivity() {

    private lateinit var mBinding: ActivityFeedbackSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityFeedbackSettingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initEvent() {
        mBinding.run {
            feedbackBackIv.setOnClickListener {
                finish()
            }
            feedbackSaveBtn.setOnClickListener {
                // 保存饮食禁忌
                val content = feedbackEt.text.toString()
                if (content.length <= 10) {
                    simpleToast("意见反馈不能少于10个字")
                    return@setOnClickListener
                }
                Repository.feedback(lifecycle = lifecycle, content = content) {
                    simpleToast(it.msg)
                }
            }
        }
    }
}