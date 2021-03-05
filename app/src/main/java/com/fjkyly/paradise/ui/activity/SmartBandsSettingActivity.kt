package com.fjkyly.paradise.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.fjkyly.paradise.base.BaseActivity
import com.fjkyly.paradise.databinding.ActivitySmartBandsSettingBinding
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.model.Facility

/**
 * 智能手环设置界面
 *
 * @property mBinding ActivitySmartBandsSettingBinding
 */
class SmartBandsSettingActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySmartBandsSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySmartBandsSettingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initEvent() {
        mBinding.run {
            smartBandsSettingBackIv.setOnClickListener {
                finish()
            }
            modifyFacilityNameTv.setOnClickListener {
                simpleToast("修改设备名称正在开发中...")
            }
            editorPermissionTv.setOnClickListener {
                simpleToast("编辑亲友查看权限正在开发中...")
            }
            familyNumberContainer.setOnClickListener {
                // startActivity<FamilyNumberActivity>()
                simpleToast("亲情号码正在开发中...")
            }
            volumeLevelContainer.setOnClickListener {
                // startActivity<VolumeSettingActivity>()
                simpleToast("音量等级正在开发中...")
            }
            tempUploadInterval.setOnClickListener {
                // startActivity<TempUploadSettingActivity>()
                simpleToast("体温上传时间间隔正在开发中...")
            }
            measuringTempContainer.setOnClickListener {
                simpleToast("测量体温正在开发中...")
            }
            addressBookWhitelistContainer.setOnClickListener {
                simpleToast("通讯录白名单正在开发中...")
            }
            heartRateUploadIntervalContainer.setOnClickListener {
                simpleToast("心率上传正在开发中...")
            }
            gpsSwitchSwt.setOnCheckedChangeListener { buttonView, isChecked ->
                simpleToast("GPS开关正在开发中...")
            }
            gpsUploadContainer.setOnClickListener {
                simpleToast("GPS上传正在开发中...")
            }
            lowPowerRemindContainer.setOnClickListener {
                simpleToast("低电提醒正在开发中...")
            }
            smsSwitchSwt.setOnCheckedChangeListener { buttonView, isChecked ->
                simpleToast("短信开关正在开发中...")
            }
            unBindFacilityBtn.setOnClickListener {
                simpleToast("解绑设备正在开发中...")
            }
        }
    }

    companion object {
        private lateinit var mFacility: Facility

        fun startActivity(context: Context, facility: Facility) {
            mFacility = facility
            ActivityUtils.startActivity(Intent(context, SmartBandsSettingActivity::class.java))
        }
    }
}