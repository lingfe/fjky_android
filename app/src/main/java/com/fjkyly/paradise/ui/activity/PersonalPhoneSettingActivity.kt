package com.fjkyly.paradise.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.databinding.ActivityPersonalPhoneSettingBinding
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.network.request.Repository
import com.vondear.rxtool.RxRegTool

class PersonalPhoneSettingActivity : MyActivity() {

    private lateinit var mBinding: ActivityPersonalPhoneSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPersonalPhoneSettingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initView() {
        mBinding.run {
            val phoneNum = intent.getStringExtra("phoneNum")
            personalPhoneEt.setText(phoneNum)
        }
    }

    override fun initEvent() {
        mBinding.run {
            personalPhoneBackIv.setOnClickListener {
                finish()
            }
            personalPhoneSaveBtn.setOnClickListener {
                // 保存手机号码
                val phoneNum = personalPhoneEt.text.toString()
                if (phoneNum.isEmpty()) {
                    simpleToast("手机号码不能为空")
                    return@setOnClickListener
                }
                if (RxRegTool.isMobile(phoneNum).not()) {
                    simpleToast("手机号码不合法")
                    return@setOnClickListener
                }
                val params = mutableMapOf<String, String>()
                params["phone"] = phoneNum
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

    companion object {
        fun startActivity(context: Context, phoneNum: String) {
            val intent = Intent(context, PersonalPhoneSettingActivity::class.java)
            intent.putExtra("phoneNum", phoneNum)
            context.startActivity(intent)
        }
    }
}