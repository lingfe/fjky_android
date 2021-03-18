package com.fjkyly.paradise.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.core.content.edit
import com.fjkyly.paradise.HomeActivity
import com.fjkyly.paradise.R
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.databinding.ActivityLoginBinding
import com.fjkyly.paradise.expand.*
import com.fjkyly.paradise.network.request.Repository
import com.fjkyly.paradise.ui.views.ConfirmDialog

/**
 * 登录界面
 */
class LoginActivity : MyActivity() {

    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var userSettingSp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initData() {
        userSettingSp = getSharedPreferences(USER_SETTING, Context.MODE_PRIVATE)
        mBinding.run {
            // 本地记住的用户账号
            loginAccountNumEt.setText(userSettingSp.getString(USER_ACCOUNT, ""))
            // 本地记住的用户密码
            loginAccountPwdEt.setText(userSettingSp.getString(USER_ACCOUNT_PWD, ""))
        }
    }

    override fun initEvent() {
        mBinding.run {
            // TODO: 2021-03-04 此处后期需要做代码优化，使用扩展函数进行设置
            loginAccountNumEt.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    loginAccountNumEt.let { editText ->
                        editText.setSelection(editText.text.toString().length)
                    }
                }
            }
            loginAccountPwdEt.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    loginAccountPwdEt.let { editText ->
                        editText.setSelection(editText.text.toString().length)
                    }
                }
            }
            loginBtn.setOnClickListener {
                // 登录按钮的点击事件
                val accountNum = loginAccountNumEt.text.toString()
                val accountPwd = loginAccountPwdEt.text.toString()
                val checkOk = checkAccountLegality(accountNum, accountPwd)
                if (checkOk.not()) return@setOnClickListener
                loginAccount(accountNum = accountNum, accountPwd = accountPwd)
            }
            // 获取是否为自动登录状态
            val auto = userSettingSp.getBoolean(AUTO_LOGIN_STATUS, false)
            // 如果是自动登录状态，则替用户点击登录按钮
            if (auto) {
                loginBtn.performClick()
            }
            registerTv.setOnClickListener {
                // 注册按钮的点击事件
                startActivity<RegisterActivity>()
            }
            forgetTv.setOnClickListener {
                // 找回密码的点击事件
                // startActivity<ModifyPwdActivity>()
                // 联系客服对话框
                val contactCustomerServiceDialog = ConfirmDialog(this@LoginActivity)
                contactCustomerServiceDialog.run {
                    setContentView(R.layout.dialog_confirm)
                    post {
                        setDialogMessage("确定拨打客服电话修改密码吗？")
                        setConfirmTextColor(Color.parseColor("#666666"))
                        setGivUpTextColor(Color.parseColor("#FF5050"))
                    }
                    show()
                }
                contactCustomerServiceDialog.setOnDialogActionClickListener(object :
                    ConfirmDialog.OnDialogActionSimpleListener() {
                    override fun onGiveUpClick() {
                        // 跳转到拨号界面，用户拨打客服电话号码修改密码
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:18585094270"))
                        startActivity(intent)
                    }
                })
            }
        }
    }

    /**
     * 登录账号
     */
    private fun loginAccount(accountNum: String, accountPwd: String) {
        // TODO: 2021/2/21 和服务器的账号数据进行匹配
        Repository.loginAccount(
            accountNum = accountNum,
            accountPwd = accountPwd,
            lifecycle = lifecycle
        ) {
            // 下次进入 APP 时自动进行登录操作，无需用户点击登录按钮
            userSettingSp.edit {
                putBoolean(AUTO_LOGIN_STATUS, true)
                putString(USER_ACCOUNT, accountNum)
                putString(USER_ACCOUNT_PWD, accountPwd)
            }
            // 跳转到主界面，并关闭登录界面
            startActivity<HomeActivity>()
            finish()
        }
    }

    /**
     * 校验输入框内容的合法性
     *
     * @param accountNum String 账号Id
     * @param accountPwd String 账号密码
     * @return Boolean 是否不为空（不为空则可以进一步校验账号和密码是否匹配，否则提示即可）
     */
    private fun checkAccountLegality(accountNum: String, accountPwd: String): Boolean {
        if (accountNum.isEmpty() || accountPwd.isEmpty()) {
            simpleToast("账号或密码为空！")
            return false
        }
        return true
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}