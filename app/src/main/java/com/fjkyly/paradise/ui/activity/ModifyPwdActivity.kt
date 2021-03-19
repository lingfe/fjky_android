package com.fjkyly.paradise.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.edit
import com.fjkyly.paradise.R
import com.fjkyly.paradise.base.App
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.databinding.ActivityModifyPwdBinding
import com.fjkyly.paradise.expand.*
import com.fjkyly.paradise.network.request.Repository
import com.vondear.rxtool.RxActivityTool

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
            modifyUserPwdAccountTv.setRoundRectBg(
                color = getColor(R.color.gray_main),
                cornerRadius = 5f.dp
            )
            modifyPwdOldUserPwdEt.setRoundRectBg(
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
            val userAccount = intent.getStringExtra("userAccount")
            modifyUserPwdAccountTv.text = userAccount
        }
    }

    override fun initEvent() {
        mBinding.run {
            modifyPwdBackIv.setOnClickListener {
                finish()
            }
            modifyPwdBtn.setOnClickListener {
                // 用户账号
                val userAccount = modifyUserPwdAccountTv.text.toString()
                // 用户原来的密码
                val oldPwd = modifyPwdOldUserPwdEt.text.toString()
                // 验证两次输入的密码是否一致
                val firstPwd = modifyPwdUserPwdEt.text.toString()
                val lastPwd = modifyPwdRepeatUserPwdEt.text.toString()
                if (userAccount.isEmpty()) {
                    simpleToast("账号不能为空")
                    return@setOnClickListener
                }
                if (oldPwd.isEmpty()) {
                    simpleToast("旧密码不能为空")
                    return@setOnClickListener
                }
                if (firstPwd.isEmpty()) {
                    simpleToast("新密码不能为空")
                    return@setOnClickListener
                }
                if (firstPwd.length < 6) {
                    simpleToast("密码不能小于6位")
                    return@setOnClickListener
                }
                if ((firstPwd matches Regex("^[A-Za-z0-9]+\$")).not()) {
                    simpleToast("密码不能含有特殊字符")
                    return@setOnClickListener
                }
                if (firstPwd != lastPwd) {
                    simpleToast("两次输入的密码不一致")
                    return@setOnClickListener
                }
                Repository.modifyUserPwd(
                    lifecycle = lifecycle,
                    phone = userAccount,
                    oldPwd = oldPwd,
                    newPwd = firstPwd
                ) {
                    simpleToast(it.msg)
                    // 密码修改成功，将自动登录状态设置为 false
                    getSharedPreferences(
                        USER_SETTING,
                        Context.MODE_PRIVATE
                    ).edit(commit = true) {
                        putBoolean(AUTO_LOGIN_STATUS, false)
                    }
                    Repository.signOut(lifecycle = lifecycle) {
                        // 账号退出成功的回调
                        RxActivityTool.skipActivityAndFinishAll(
                            this@ModifyPwdActivity,
                            LoginActivity::class.java
                        )
                        // 退出登录之后，需要将用户信息置空
                        App.accountLoginInfo = null
                    }
                }
            }
        }
    }

    companion object {
        fun startActivity(context: Context, userAccount: String) {
            val intent = Intent(context, ModifyPwdActivity::class.java)
            intent.putExtra("userAccount", userAccount)
            context.startActivity(intent)
        }
    }
}