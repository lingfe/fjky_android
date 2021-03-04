package com.fjkyly.paradise.ui.activity

import android.os.Bundle
import android.util.Log
import com.blankj.utilcode.util.GsonUtils
import com.fjkyly.paradise.base.App
import com.fjkyly.paradise.base.BaseActivity
import com.fjkyly.paradise.databinding.ActivityUserNameSettingBinding
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.model.HTTP_OK
import com.fjkyly.paradise.model.ModifyUserName
import com.fjkyly.paradise.network.request.Repository
import com.fjkyly.paradise.network.request.ServiceCreator
import com.fjkyly.paradise.network.request.api.ModifyUserNameApi
import retrofit2.Call
import retrofit2.Response

/**
 * 用户名设置界面
 */
class UserNameSettingActivity : BaseActivity() {

    private lateinit var mBinding: ActivityUserNameSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityUserNameSettingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initEvent() {
        mBinding.run {
            userNameSettingBackIv.setOnClickListener {
                finish()
            }
            saveUserNameTv.setOnClickListener {
                // TODO: 2021/2/28 通过请求接口修改用户名
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
    }
}