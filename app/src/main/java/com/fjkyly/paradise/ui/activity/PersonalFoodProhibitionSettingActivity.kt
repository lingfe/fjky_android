package com.fjkyly.paradise.ui.activity

import android.os.Bundle
import com.fjkyly.paradise.base.BaseActivity
import com.fjkyly.paradise.databinding.ActivityPersonalFoodProhibitionSettingBinding
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.network.request.Repository

class PersonalFoodProhibitionSettingActivity : BaseActivity() {

    private lateinit var mBinding: ActivityPersonalFoodProhibitionSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPersonalFoodProhibitionSettingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initData() {
        Repository.queryPersonBasicInfo(lifecycle = lifecycle) {
            val data = it.data
            mBinding.personalFoodProhibitionEt.setText(data.dietTaboo)
        }
    }

    override fun initEvent() {
        mBinding.run {
            personalFoodProhibitionBackIv.setOnClickListener {
                finish()
            }
            personalFoodProhibitionSaveBtn.setOnClickListener {
                // 保存饮食禁忌
                val personalFoodProhibition = personalFoodProhibitionEt.text.toString()
                val params = mutableMapOf<String, String>()
                params["diet_taboo"] = personalFoodProhibition
                Repository.modifyPersonBasicInfo(
                    lifecycle = lifecycle,
                    params = params,
                ) {
                    simpleToast(it.msg)
                }
            }
        }
    }
}