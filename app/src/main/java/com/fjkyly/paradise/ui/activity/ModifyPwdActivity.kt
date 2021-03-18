package com.fjkyly.paradise.ui.activity

import android.os.Bundle
import com.fjkyly.paradise.R
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.databinding.ActivityModifyPwdBinding
import com.fjkyly.paradise.expand.dp
import com.fjkyly.paradise.expand.setRoundRectBg
import com.fjkyly.paradise.expand.simpleToast

/**
 * 修改密码界面（与找回密码功能通用）
 */
class ModifyPwdActivity : MyActivity() {

    private var _binding: ActivityModifyPwdBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityModifyPwdBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initView() {
        mBinding.run {
            modifyUserPwdAccountEt.setRoundRectBg(
                color = getColor(R.color.gray_main),
                cornerRadius = 5f.dp
            )
            modifyPwdUserPwdEt.setRoundRectBg(
                color = getColor(R.color.gray_main),
                cornerRadius = 5f.dp
            )
            modifyPwdRepeatUserPwdEt.setRoundRectBg(
                color = getColor(R.color.gray_main),
                cornerRadius = 5f.dp
            )
        }
    }

    override fun initEvent() {
        mBinding.run {
            modifyPwdBtn.setOnClickListener {
                val userAccount = modifyUserPwdAccountEt.text.toString()
                // 验证两次输入的密码是否一致
                val firstPwd= modifyPwdUserPwdEt.text.toString()
                val lastPwd = modifyPwdRepeatUserPwdEt.text.toString()
                if (firstPwd != lastPwd) {
                    simpleToast("两次输入的密码不一致")
                    return@setOnClickListener
                }
                // TODO: 2021-03-16 获取用户输入的验证码交给后端进行验证
            }
        }
    }
}