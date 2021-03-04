package com.fjkyly.paradise.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils.startActivity
import com.fjkyly.paradise.base.BaseActivity
import com.fjkyly.paradise.databinding.ActivitySmartBandsBinding
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.expand.startActivity
import com.fjkyly.paradise.model.Facility

/**
 * 智能手环界面
 *
 * @property mBinding ActivitySmartBandsBinding
 */
class SmartBandsActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySmartBandsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySmartBandsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initView() {
        mBinding.run {
            smartBandsNameTv.text = mFacility.name
        }
    }

    override fun initEvent() {
        mBinding.run {
            smartBandsBackIv.setOnClickListener {
                finish()
            }
            smartBandsSettingTv.setOnClickListener {
                startActivity<SmartBandsSettingActivity>()
                simpleToast("设置智能手环功能正在开发中...")
            }
            smartBandsLocationContainer.setOnClickListener {
                simpleToast("手环定位功能正在开发中...")
            }
        }
    }

    companion object {
        private lateinit var mFacility: Facility

        fun startActivity(context: Context, facility: Facility) {
            mFacility = facility
            startActivity(Intent(context, SmartBandsActivity::class.java))
        }
    }
}