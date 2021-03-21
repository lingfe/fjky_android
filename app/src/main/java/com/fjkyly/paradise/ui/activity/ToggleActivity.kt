package com.fjkyly.paradise.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.databinding.ActivityToggleBinding
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.model.DeviceSettingFun
import com.fjkyly.paradise.network.request.Repository

/**
 * 通用的开关界面
 */
class ToggleActivity : MyActivity() {

    private var _binding: ActivityToggleBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityToggleBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initView() {
        mBinding.run {
            toggleTitleTv.text = mDeviceFun.funName
        }
    }

    override fun initEvent() {
        mBinding.run {
            toggleBackIv.setOnClickListener {
                finish()
            }
            saveBtn.setOnClickListener {
                val isChecked = toggleBtn.isChecked
                // 此处的设备功能类型有两种情况，"0"和"1" 或则 "true"和"false"
                val deviceFunValue = when (mDeviceFun.funValueType) {
                    2 -> isChecked.toString()
                    10 -> if (isChecked) "1" else "0"
                    else -> isChecked.toString()
                }
                simpleToast("指令已发送")
                // 调用后端接口，修改功能参数数据
                Repository.modifyDeviceFunValue(
                    deviceFunId = mDeviceFun.id,
                    deviceFunValue = deviceFunValue,
                    lifecycle = lifecycle
                ) {
                    // 指令发送成功
                    simpleToast(it.msg)
                }
            }
        }
    }

    companion object {
        private lateinit var mDeviceFun: DeviceSettingFun.Data.DevFun

        fun startActivity(context: Context, deviceFun: DeviceSettingFun.Data.DevFun) {
            mDeviceFun = deviceFun
            ActivityUtils.startActivity(Intent(context, ToggleActivity::class.java))
        }
    }
}