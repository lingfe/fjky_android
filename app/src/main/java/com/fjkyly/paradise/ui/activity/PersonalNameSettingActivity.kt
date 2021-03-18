package com.fjkyly.paradise.ui.activity

import android.os.Bundle
import com.fjkyly.paradise.base.BaseActivity
import com.fjkyly.paradise.databinding.ActivityPersonalNameSettingBinding
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.network.request.Repository

class PersonalNameSettingActivity : BaseActivity() {

    private lateinit var mBinding: ActivityPersonalNameSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPersonalNameSettingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initEvent() {
        mBinding.run {
            personalNameBackIv.setOnClickListener {
                finish()
            }

            personalNameSaveBtn.setOnClickListener {
                // 保存姓名
                val personalName = personalNameEt.text.toString()
                if (personalName.isEmpty()) {
                    simpleToast("姓名不能为空")
                    return@setOnClickListener
                }
                val params = mutableMapOf<String, String>()
                params["full_name"] = personalName
                Repository.modifyPersonBasicInfo(
                    lifecycle = lifecycle,
                    params = params,
                ) {
                    simpleToast(it.msg)
                    finish()
                }
            }
        }
    }
}