package com.fjkyly.paradise.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.databinding.ActivityPersonalIdCardSettingBinding
import com.fjkyly.paradise.expand.getAgeByBirth
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.network.request.Repository
import com.vondear.rxtool.RxRegTool
import java.text.SimpleDateFormat
import java.util.*

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
                val idCardParams = mutableMapOf<String, String>()
                idCardParams["id_card"] = idCard
                Repository.modifyPersonBasicInfo(
                    lifecycle = lifecycle,
                    params = idCardParams,
                ) {
                    simpleToast(it.msg)
                    finish()
                    // 计算性别
                    val sexParams = mutableMapOf<String, String>()
                    val sex =
                        idCard.substring(idCard.length - 2, idCard.length - 1).toIntOrNull() ?: 1
                    sexParams["gender"] = if (sex % 2 != 0) "男" else "女"
                    Repository.modifyPersonBasicInfo(
                        lifecycle = lifecycle,
                        params = sexParams
                    ) {}
                    // 计算出生日期
                    val bornDateStr = idCard.substring(6, 14)
                    Log.d(TAG, "initEvent: ===>bornDateStr：$bornDateStr")
                    val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
                    val bornDate = sdf.parse(bornDateStr)
                    if (bornDate != null) {
                        // 计算年龄
                        val ageParams = mutableMapOf<String, String>()
                        val age = getAgeByBirth(bornDate)
                        ageParams["age"] = age.toString()
                        Repository.modifyPersonBasicInfo(
                            lifecycle = lifecycle,
                            params = ageParams
                        ) {
                            Log.d(TAG, "initEvent: ===>年龄修改成功 ===>age：$age")
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "PersonalIdCardSettingAc"
        fun startActivity(context: Context, idCard: String) {
            val intent = Intent(context, PersonalIdCardSettingActivity::class.java)
            intent.putExtra("idCard", idCard)
            context.startActivity(intent)
        }
    }
}