package com.fjkyly.paradise.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.fjkyly.paradise.base.App
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.databinding.ActivityUserNameSettingBinding
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.network.request.Repository

/**
 * 用户名设置界面
 */
class UserNameSettingActivity : MyActivity() {

    private lateinit var mBinding: ActivityUserNameSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityUserNameSettingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initView() {
        mBinding.run {
            val userName = intent.getStringExtra("userName")
            userNameEt.setText(userName)
        }
    }

    override fun initEvent() {
        mBinding.run {
            userNameSettingBackIv.setOnClickListener {
                finish()
            }
            saveUserNameTv.setOnClickListener {
                // 通过请求接口修改用户名
                val newUserName = userNameEt.text.toString()
                if (newUserName.length < 2 || newUserName.length > 12) {
                    simpleToast("请输入2-12位的用户名")
                    return@setOnClickListener
                }
                Repository.modifyUserName(newUserName = newUserName, lifecycle = lifecycle) {
                    App.accountLoginInfo?.let {
                        it.data.userInfo.username = newUserName
                    }
                    simpleToast("用户名修改成功！")
                }
            }
            clearUserNameIv.setOnClickListener {
                userNameEt.setText("")
            }
        }
    }

    companion object {
        private const val TAG = "UserNameSettingActivity"

        fun startActivity(context: Context, userName: String) {
            val intent = Intent(context, UserNameSettingActivity::class.java)
            intent.putExtra("userName", userName)
            context.startActivity(intent)
        }
    }
}