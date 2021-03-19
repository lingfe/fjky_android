package com.fjkyly.paradise.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.databinding.ActivityPersonalIdCardSettingBinding
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.network.request.Repository
import com.vondear.rxtool.RxRegTool

class PersonalIdCardSettingActivity : MyActivity() {

    private lateinit var mBinding: ActivityPersonalIdCardSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPersonalIdCardSettingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initView() {
        mBinding.run {
            val idCard = intent.getStringExtra("idCard")
            personalIdCardEt.setText(idCard)
        }
    }

    override fun initEvent() {
        mBinding.run {
            personalIdCardBackIv.setOnClickListener {
                finish()
            }
            personalIdCardSaveBtn.setOnClickListener {
                // 保存身份证号码
                val idCard = personalIdCardEt.text.toString()
                if (idCard.isEmpty()) {
                    simpleToast("身份证号码不能为空")
                    return@setOnClickListener
                }
                if (RxRegTool.isIDCard(idCard).not()) {
                    simpleToast("身份证号码不合法")
                    return@setOnClickListener
                }
                val params = mutableMapOf<String, String>()
                params["id_card"] = idCard
                Repository.modifyPersonBasicInfo(
                    lifecycle = lifecycle,
                    params = params,
                ) {
                    simpleToast(it.msg)
                    finish()
                }
            }
        }
    }

    companion object {
        fun startActivity(context: Context, idCard: String) {
            val intent = Intent(context, PersonalIdCardSettingActivity::class.java)
            intent.putExtra("idCard", idCard)
            context.startActivity(intent)
        }
    }
}