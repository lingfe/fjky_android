package com.fjkyly.paradise.ui.activity

import android.os.Bundle
import com.fjkyly.paradise.base.BaseActivity
import com.fjkyly.paradise.databinding.ActivityCustomerTypeBinding
import java.util.*

/**
 * 客户类别选择界面
 */
class CustomerTypeActivity : BaseActivity() {

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