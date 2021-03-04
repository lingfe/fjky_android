package com.fjkyly.paradise.ui.activity

import android.os.Bundle
import android.view.View
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.fjkyly.paradise.base.BaseActivity
import com.fjkyly.paradise.databinding.ActivityPersonalDetailsBinding
import com.fjkyly.paradise.expand.startActivity
import java.util.*

/**
 * 个人详细信息界面
 */
class PersonalDetailsActivity : BaseActivity() {

    private lateinit var mBinding: ActivityPersonalDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPersonalDetailsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initEvent() {
        mBinding.run {
            personalDetailsBackIv.setOnClickListener {
                finish()
            }
            personalNameContainer.setOnClickListener {
                // TODO: 2021/2/28 姓名
                startActivity<PersonalNameActivity>()
            }
            personalSexContainer.setOnClickListener {
                // TODO: 2021/2/28 性别选择
                val sexList = mutableListOf<String>().apply {
                    add("男")
                    add("女")
                }
                OptionsPickerBuilder(this@PersonalDetailsActivity) { options1: Int, _: Int, _: Int, _: View? ->
                    val sex = sexList[options1]
                    personalSexTv.text = sex
                }
                    .isAlphaGradient(true)
                    .build<String>()
                    .apply {
                        setOnDismissListener {
                            sexList.clear()
                        }
                        setPicker(sexList)
                        show()
                    }
            }
            personalIdentityNumberContainer.setOnClickListener {
                // TODO: 2021/2/28 身份证号码
            }
            personalAgeContainer.setOnClickListener {
                // TODO: 2021/2/28 年龄选择
                val ageList = mutableListOf<String>().apply {
                    for (age in 0..50) {
                        add((age + 50).toString())
                    }
                }
                OptionsPickerBuilder(this@PersonalDetailsActivity) { options1: Int, _: Int, _: Int, _: View? ->
                    val age = ageList[options1]
                    personalAgeTv.text = age
                }
                    .isAlphaGradient(true)
                    .build<String>()
                    .apply {
                        setOnDismissListener {
                            ageList.clear()
                        }
                        setPicker(ageList)
                        show()
                    }
            }
            personalPhoneNumberContainer.setOnClickListener {
                // TODO: 2021/2/28 手机号
            }
            personalCustomerTypeContainer.setOnClickListener {
                // TODO: 2021/2/28 客户类别
            }
            personalMaritalStatusContainer.setOnClickListener {
                // TODO: 2021/2/28 婚姻情况
            }
            personalLivingSituationContainer.setOnClickListener {
                // TODO: 2021/2/28 居住情况
            }
            personalHomeAddressContainer.setOnClickListener {
                // TODO: 2021/2/28 家庭住址
            }
            personalEmergencyContactContainer.setOnClickListener {
                // TODO: 2021/2/28 紧急联系人
            }
            personalEmergencyContactPhoneContainer.setOnClickListener {
                // TODO: 2021/2/28 紧急联系人电话
            }
            personalCommunityContainer.setOnClickListener {
                // TODO: 2021/2/28 所属社区
            }
        }
    }
}