package com.fjkyly.paradise.ui.activity

import android.os.Bundle
import com.fjkyly.paradise.base.BaseActivity
import com.fjkyly.paradise.databinding.ActivityPersonalNameBinding
import com.fjkyly.paradise.expand.simpleToast

class PersonalNameActivity : BaseActivity() {

    private lateinit var mBinding: ActivityPersonalNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPersonalNameBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initEvent() {
        mBinding.run {
            personalNameBackIv.setOnClickListener {
                finish()
            }
            personalNameSaveBtn.setOnClickListener {
                // TODO: 2021/2/28 保存姓名
                simpleToast("保存姓名正在开发中...")
            }
        }
    }
}