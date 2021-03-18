package com.fjkyly.paradise.ui.activity

import android.os.Bundle
import android.view.View
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.fjkyly.paradise.R
import com.fjkyly.paradise.base.BaseActivity
import com.fjkyly.paradise.databinding.ActivityPersonalDetailsBinding
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.expand.startActivity
import com.fjkyly.paradise.network.request.Repository
import com.vondear.rxtool.RxTimeTool

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

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        mBinding.run {
            Repository.queryPersonBasicInfo(lifecycle = lifecycle) {
                val data = it.data
                // 姓名
                personalNameTv.text = data.fullName
                // 性别，默认为“男”
                personalSexTv.text = data.gender.ifEmpty { "男" }
                // 身份证号码
                personalIdentityNumberTv.text = data.idCard
                // 生日
                personalBirthdayTv.text = data.birthday.ifEmpty { "设置您的生日" }
                // 年龄
                personalAgeTv.text = data.age.ifEmpty { "0" }
                // 名族默认为“汉”
                personalNationTv.text = data.nation.ifEmpty { "汉" }
                // 联系电话
                personalPhoneNumberTv.text = data.phone
            }
        }
    }

    override fun initEvent() {
        mBinding.run {
            personalDetailsBackIv.setOnClickListener {
                finish()
            }
            personalNameContainer.setOnClickListener {
                // 跳转到姓名设置界面
                startActivity<PersonalNameSettingActivity>()
            }
            personalSexContainer.setOnClickListener {
                // 性别选择
                val sexList = mutableListOf<String>().apply {
                    add("男")
                    add("女")
                }
                OptionsPickerBuilder(this@PersonalDetailsActivity) { options1: Int, _: Int, _: Int, _: View? ->
                    val sex = sexList[options1]
                    modifyParams("gender", sex)
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
                // 身份证号码设置
                startActivity<PersonalIdCardSettingActivity>()
            }
            personalBirthdayContainer.setOnClickListener {
                TimePickerBuilder(this@PersonalDetailsActivity) { date, _ ->
                    val format = RxTimeTool.simpleDateFormat("MM月dd日", date)
                    personalBirthdayTv.text = format
                    modifyParams("birthday", format)
                }
                    .setType(booleanArrayOf(false, true, true, false, false, false))
                    .build()
                    .show()
            }
            personalAgeContainer.setOnClickListener {
                // 年龄选择
                val ageList = mutableListOf<String>().apply {
                    for (age in 0..130) {
                        add((age).toString())
                    }
                }
                OptionsPickerBuilder(this@PersonalDetailsActivity) { options1: Int, _: Int, _: Int, _: View? ->
                    val age = ageList[options1]
                    personalAgeTv.text = age
                    modifyParams("age", age)
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
            personalNationContainer.setOnClickListener {
                // 56个民族的选择
                val nationList = arrayListOf<String>()
                nationList.addAll(resources.getStringArray(R.array.nationalityArray))
                OptionsPickerBuilder(this@PersonalDetailsActivity) { options1: Int, _: Int, _: Int, _: View? ->
                    val nation = nationList[options1]
                    modifyParams("nation", nation)
                }
                    .isAlphaGradient(true)
                    .build<String>()
                    .apply {
                        setOnDismissListener {
                            nationList.clear()
                        }
                        setPicker(nationList)
                        show()
                    }
            }
            personalPhoneNumberContainer.setOnClickListener {
                // 联系电话的手机号码设置
                startActivity<PersonalPhoneSettingActivity>()
            }
        }
    }

    /**
     * 修改参数
     */
    private fun modifyParams(paramName: String, paramValue: String) {
        val params = mutableMapOf<String, String>()
        params[paramName] = paramValue
        Repository.modifyPersonBasicInfo(
            lifecycle = lifecycle,
            params = params,
        ) {
            // 修改成功之后，刷新界面数据
            loadData()
            simpleToast(it.msg)
        }
    }

    companion object {
        private const val TAG = "PersonalDetailsActivity"
    }
}