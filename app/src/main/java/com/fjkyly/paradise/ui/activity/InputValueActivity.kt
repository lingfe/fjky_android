package com.fjkyly.paradise.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.databinding.ActivityInputValueBinding
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.model.DeviceSettingFun
import com.fjkyly.paradise.network.request.Repository

class InputValueActivity : MyActivity() {

    private var _binding: ActivityInputValueBinding? = null
    private val mBinding get() = _binding!!
    private var mDeviceFunValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityInputValueBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initView() {
        mBinding.run {
            inputValueTitleTv.text = mDeviceFun.funName
            inputValuePromptTextTv.text = mDeviceFun.funExplain
        }
    }

    override fun initEvent() {
        mBinding.run {
            inputValueBackIv.setOnClickListener {
                finish()
            }
            clearInputValueIv.setOnClickListener {
                inputValueEt.setText("")
            }
            saveBtn.setOnClickListener {
                // 简单检查一下用户输入的值是否满足条件
                checkInputValueLegality { isOk ->
                    if (isOk.not()) {
                        simpleToast("输入不合法，请检查后重试")
                        return@checkInputValueLegality
                    }
                    // 调用后端接口，修改功能参数数据
                    Repository.modifyDeviceFunValue(
                        deviceFunId = mDeviceFun.id,
                        deviceFunValue = mDeviceFunValue,
                        lifecycle = lifecycle
                    ) {
                        // 指令发送成功
                        simpleToast(it.msg)
                    }
                }
            }
        }
    }

    /**
     * 检查输入值的合法性
     */
    private fun ActivityInputValueBinding.checkInputValueLegality(block: (ok: Boolean) -> Unit) {
        val inputValue = inputValueEt.text.toString()
        val isOk = when (mDeviceFun.funValueType) {
            // 没有条件的纯文本，无需验证，直接返回 true
            0 -> true
            3 -> inputValue.contains(Regex("~"))
            5 -> inputValue.contains(Regex(","))
            6 -> inputValue.contains(Regex(","))
            7 -> inputValue.contains(Regex("-"))
            8 -> inputValue.contains(Regex("-"))
            // 如果不在定义的范围之内，直接返回 false
            else -> false
        }
        block(isOk)
    }

    companion object {
        private lateinit var mDeviceFun: DeviceSettingFun.Data.DevFun

        fun startActivity(context: Context, deviceFun: DeviceSettingFun.Data.DevFun) {
            mDeviceFun = deviceFun
            ActivityUtils.startActivity(Intent(context, InputValueActivity::class.java))
        }
    }
}