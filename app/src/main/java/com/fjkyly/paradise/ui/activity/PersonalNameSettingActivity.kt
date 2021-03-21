package com.fjkyly.paradise.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.databinding.ActivityPersonalNameSettingBinding
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.network.request.Repository

class PersonalNameSettingActivity : MyActivity() {

    private lateinit var mBinding: ActivityPersonalNameSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPersonalNameSettingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initView() {
        mBinding.run {
            val personalName = intent.getStringExtra("personalName")
            personalNameEt.setText(personalName)
        }
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
                // 限制中英文和数字
                if ((personalName matches Regex("^[\\u4e00-\\u9fa5_a-zA-Z0-9]+\$")).not()) {
                    simpleToast("姓名不得包含特殊字符")
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

    companion object {
        fun startActivity(context: Context, personalName: String) {
            val intent = Intent(context, PersonalNameSettingActivity::class.java)
            intent.putExtra("personalName", personalName)
            context.startActivity(intent)
        }
    }
}