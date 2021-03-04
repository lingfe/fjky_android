package com.fjkyly.paradise.ui.activity

import android.os.Bundle
import com.fjkyly.paradise.base.BaseActivity
import com.fjkyly.paradise.databinding.ActivityAddKinsfolkBinding
import com.fjkyly.paradise.expand.simpleToast

class AddKinsfolkActivity : BaseActivity() {

    private lateinit var mBinding: ActivityAddKinsfolkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddKinsfolkBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initEvent() {
        mBinding.run {
            addFamilyNumberBackIv.setOnClickListener {
                finish()
            }
            saveBtn.setOnClickListener {
                simpleToast("新增亲友正在开发中...")
            }
        }
    }
}