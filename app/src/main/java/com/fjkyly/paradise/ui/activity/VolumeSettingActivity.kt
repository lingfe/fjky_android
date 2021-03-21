package com.fjkyly.paradise.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import com.blankj.utilcode.util.ActivityUtils
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.databinding.ActivityVolumeSettingBinding
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.model.DeviceSettingFun
import com.fjkyly.paradise.network.request.Repository

class VolumeSettingActivity : MyActivity() {

    private lateinit var mBinding: ActivityVolumeSettingBinding
    private var mCurrentProgress = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityVolumeSettingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initView() {
        mBinding.run {
            volumeTitleTv.text = mDeviceFun.funName
            updateProgress()
        }
    }

    override fun initEvent() {
        mBinding.run {
            volumeBackIv.setOnClickListener {
                finish()
            }
            volumeChangeSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    mCurrentProgress = progress
                    updateProgress()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }
            })
            saveBtn.setOnClickListener {
                simpleToast("指令已发送")
                // 调用后端接口，修改功能参数数据
                Repository.modifyDeviceFunValue(
                    deviceFunId = mDeviceFun.id,
                    deviceFunValue = mCurrentProgress.toString(),
                    lifecycle = lifecycle
                ) {
                    // 指令发送成功
                    simpleToast(it.msg)
                }
            }
        }
    }

    private fun ActivityVolumeSettingBinding.updateProgress() {
        volumeValueTv.text = mCurrentProgress.toString()
    }

    companion object {
        private lateinit var mDeviceFun: DeviceSettingFun.Data.DevFun

        fun startActivity(context: Context, deviceFun: DeviceSettingFun.Data.DevFun) {
            mDeviceFun = deviceFun
            ActivityUtils.startActivity(Intent(context, VolumeSettingActivity::class.java))
        }
    }
}