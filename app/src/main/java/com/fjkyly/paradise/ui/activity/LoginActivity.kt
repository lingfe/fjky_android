package com.fjkyly.paradise.ui.activity

import android.os.Bundle
import com.fjkyly.paradise.HomeActivity
import com.fjkyly.paradise.base.BaseActivity
import com.fjkyly.paradise.databinding.ActivityLoginBinding
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.expand.startActivity
import com.fjkyly.paradise.network.request.Repository

/**
 * 登录界面
 */
class LoginActivity : BaseActivity() {

    private lateinit var mBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initEvent() {
        mBinding.run {
            // TODO: 2021-03-04 此处后期需要做代码优化，使用扩展函数进行设置
            loginAccountNumEt.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus){
                    loginAccountNumEt.let { editText ->
                        editText.setSelection(editText.text.toString().length)
                    }
                }
            }
            loginAccountPwdEt.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus){
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
            // TODO: 2021-03-04 模拟点击登录按钮，后期上线需要删除掉该代码
            // loginBtn.performClick()
            registerTv.setOnClickListener {
                // 注册按钮的点击事件
                startActivity<RegisterActivity>()
            }
            forgetTv.setOnClickListener {
                // 找回密码的点击事件
                simpleToast("跳转到密码找回界面")
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
            startActivity<HomeActivity>()
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