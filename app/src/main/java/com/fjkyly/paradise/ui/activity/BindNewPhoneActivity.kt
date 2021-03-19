package com.fjkyly.paradise.ui.activity

import android.os.Bundle
import android.view.View
import com.fjkyly.paradise.R
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.databinding.ActivityBindNewPhoneBinding
import com.fjkyly.paradise.expand.dp
import com.fjkyly.paradise.expand.setRoundRectBg
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.network.request.Repository
import com.vondear.rxtool.RxRegTool

class BindNewPhoneActivity : MyActivity() {

    private lateinit var mBinding: ActivityBindNewPhoneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityBindNewPhoneBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initView() {
        mBinding.run {
            bindNewPhoneEt.setRoundRectBg(
                color = getColor(R.color.gray_main),
                cornerRadius = 5f.dp
            )
        }
    }

    override fun initEvent() {
        mBinding.run {
            bindNewPhoneBackIv.setOnClickListener {
                finish()
            }
            bindNewPhoneBtn.setOnClickListener {
                val newPhone = bindNewPhoneEt.text.toString()
                if (RxRegTool.isMobile(newPhone).not()) {
                    simpleToast("请输入正确的手机号")
                    return@setOnClickListener
                }
                // 绑定手机号码
                Repository.modifyUserPhoneBind(
                    lifecycle = lifecycle,
                    newPhone = newPhone
                ) {
                    bindNewPhoneContainer.visibility = View.GONE
                    bindNewPhoneSuccessContainer.visibility = View.VISIBLE
                }
            }
            backAccountMangerBtn.setOnClickListener {
                finish()
            }
        }
    }
}