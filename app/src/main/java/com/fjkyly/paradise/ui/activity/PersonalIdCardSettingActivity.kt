package com.fjkyly.paradise.ui.activity

import android.os.Bundle
import com.fjkyly.paradise.base.BaseActivity
import com.fjkyly.paradise.databinding.ActivityPersonalIdCardSettingBinding
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.network.request.Repository
import com.vondear.rxtool.RxRegTool

class PersonalIdCardSettingActivity : BaseActivity() {

    private lateinit var mBinding: ActivityPersonalIdCardSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPersonalIdCardSettingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initEvent() {
        mBinding.run {
            personalIdCardBackIv.setOnClickListener {
                finish()
            }
            personalIdCardSaveBtn.setOnClickListener {
                // 保存身份证号码
                val idCard = personalIdCardEt.text.toString()
                if (idCard.isEmpty()) {
                    simpleToast("身份证号码不能为空")
                    return@setOnClickListener
                }
                if (RxRegTool.isIDCard(idCard).not()) {
                    simpleToast("身份证号码不合法")
                    return@setOnClickListener
                }
                val params = mutableMapOf<String, String>()
                params["id_card"] = idCard
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