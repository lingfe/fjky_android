package com.fjkyly.paradise.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.databinding.ActivityRangeRulerBinding
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.model.DeviceSettingFun
import com.fjkyly.paradise.network.request.Repository

class RangeRulerActivity : MyActivity() {

    private var _binding: ActivityRangeRulerBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRangeRulerBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        mBinding.run {
            rangeRulerTitleTv.text = "${mDeviceFun.funName}设置"
        }
    }

    override fun initEvent() {
        mBinding.run {
            rangeRulerBackIv.setOnClickListener {
                finish()
            }
            saveBtn.setOnClickListener {
                // 获取用户设置的值
                val totalValue = rangeRulerView.currentValue
                // 调用后端接口，修改功能参数数据
                Repository.modifyDeviceFunValue(
                    deviceFunId = mDeviceFun.id,
                    deviceFunValue = totalValue.toString(),
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
            ActivityUtils.startActivity(Intent(context, RangeRulerActivity::class.java))
        }
    }
}