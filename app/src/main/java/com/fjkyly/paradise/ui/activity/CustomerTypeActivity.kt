package com.fjkyly.paradise.ui.activity

import android.os.Bundle
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.databinding.ActivityCustomerTypeBinding

class CustomerTypeActivity : MyActivity() {

    private lateinit var mBinding: ActivityCustomerTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCustomerTypeBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initEvent() {
        mBinding.run {
            customerTypeBackIv.setOnClickListener {
                finish()
            }
        }
    }
}