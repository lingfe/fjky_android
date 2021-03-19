package com.fjkyly.paradise.ui.activity

import android.graphics.Color
import android.os.Bundle
import com.fjkyly.paradise.R
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.databinding.ActivityRegisterBinding
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.expand.startActivity
import com.fjkyly.paradise.network.request.Repository
import com.fjkyly.paradise.ui.views.ConfirmDialog
import com.vondear.rxtool.RxConstTool

/**
 * 注册界面
 */
class RegisterActivity : MyActivity() {

    private lateinit var mBinding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initEvent() {
        mBinding.run {
            registerBackIv.setOnClickListener {
                finish()
            }
            nextBtn.setOnClickListener {
                // 通过请求服务器数据，验证用户输入的手机号码和验证码是否正确，
                // 如果匹配则提示用户注册成功并返回登录界面，否则提示用户手机号或验证码错误
                val phoneNum = regPhoneNumEt.text.toString()
                val pwd = regPwdEt.text.toString()
                val rePwd = regRePwdEt.text.toString()
                val idCard = regIdCardEt.text.toString()
                val verifyCode = regVerifyCodeEt.text.toString()
                val mobilePatternOk = phoneNum matches Regex(RxConstTool.REGEX_MOBILE_EXACT)
                if (mobilePatternOk.not()) {
                    simpleToast("手机号码格式不正确")
                    return@setOnClickListener
                }
                if (pwd.isEmpty() || idCard.isEmpty() || verifyCode.isEmpty()) {
                    simpleToast("请将信息填写完整")
                    return@setOnClickListener
                }
                if (pwd.length < 6) {
                    simpleToast("密码不能小于6位")
                    return@setOnClickListener
                }
                if ((pwd matches Regex("^[A-Za-z0-9]+\$")).not()) {
                    simpleToast("密码不能含有特殊字符")
                    return@setOnClickListener
                }
                if ((pwd == rePwd).not()) {
                    simpleToast("两次输入的密码不一致")
                    return@setOnClickListener
                }
                if ((idCard matches Regex(RxConstTool.REGEX_IDCARD18)).not()) {
                    simpleToast("请输入正确的身份证号码")
                    return@setOnClickListener
                }
                // 手机号码格式正确，将手机号和验证码进行匹配
                checkAccountInfo(
                    phoneNum = phoneNum,
                    pwd = pwd,
                    idCard = idCard,
                    verifyCode = verifyCode
                )
            }
            userAgreementTv.setOnClickListener {
                // 跳转到用户协议界面
                startActivity<UserAgreementActivity>()
            }
        }
    }

    /**
     * 检查手机号码和验证码是否匹配
     *
     * @param phoneNum String
     * @param verifyCode String
     */
    private fun checkAccountInfo(
        phoneNum: String,
        pwd: String,
        idCard: String,
        verifyCode: String
    ) {
        Repository.registerAccount(
            phoneNum = phoneNum,
            pwd = pwd,
            idCard = idCard,
            lifecycle = lifecycle
        ) {
            val backDialog = ConfirmDialog(this@RegisterActivity)
            backDialog.run {
                setContentView(R.layout.dialog_confirm)
                post {
                    setDialogMessage("注册成功，是否返回登录界面？")
                    setConfirmTextColor(Color.parseColor("#666666"))
                    setGivUpTextColor(Color.parseColor("#FF5050"))
                }
                show()
            }
            backDialog.setOnDialogActionClickListener(object :
                ConfirmDialog.OnDialogActionSimpleListener() {
                override fun onGiveUpClick() {
                    finish()
                }
            })
        }
    }
}