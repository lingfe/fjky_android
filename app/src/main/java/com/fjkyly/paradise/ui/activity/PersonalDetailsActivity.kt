package com.fjkyly.paradise.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.Glide
import com.fjkyly.paradise.R
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.databinding.ActivityPersonalDetailsBinding
import com.fjkyly.paradise.expand.*
import com.fjkyly.paradise.network.request.Repository
import com.fjkyly.paradise.ui.views.AddressDialog
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.vondear.rxtool.RxTimeTool
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy
import java.text.SimpleDateFormat
import java.util.*

class PersonalDetailsActivity : MyActivity() {

    private lateinit var mBinding: ActivityPersonalDetailsBinding

    /** 省  */
    private var mProvince = "贵州省"

    /** 市  */
    private var mCity = "铜仁市"

    /** 区  */
    private var mArea = "碧江区"

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
                // 默认头像
                var personalRealAvatar = data.img
                if (TextUtils.isEmpty(personalRealAvatar)) {
                    personalRealAvatar =
                        "https://dss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2561659095,299912888&fm=26&gp=0.jpg"
                }
                Glide.with(this@PersonalDetailsActivity)
                    .load(personalRealAvatar)
                    .into(personalRealAvatarIv)
                // 姓名
                personalNameTv.text = data.fullName
                // 性别，默认为“男”
                val sex = data.gender
                if (TextUtils.isEmpty(sex)) {
                    personalSexTv.hint = "请选择您的性别"
                } else {
                    personalSexTv.text = sex
                }
                // 身份证号码
                personalIdentityNumberTv.text = data.idCard
                // 生日
                val birthday = data.birthday
                if (TextUtils.isEmpty(birthday)) {
                    personalBirthdayTv.hint = "请设置您的生日"
                } else {
                    personalBirthdayTv.text = birthday
                }
                // 年龄
                val age = data.age
                if (TextUtils.isEmpty(age)) {
                    personalAgeTv.hint = "请选择您的年龄"
                } else {
                    personalAgeTv.text = age
                }
                // 民族默认为“汉”
                val nation = data.nation
                if (TextUtils.isEmpty(nation)) {
                    personalNationTv.hint = "请选择您的民族"
                } else {
                    personalNationTv.text = nation
                }
                // 户籍地址
                personalHomeAddressTv.text = data.permanentAddress
                // 联系电话
                personalPhoneNumberTv.text = data.phone
                // 爱好
                personalHobbyTv.text = data.hobby
                // 饮食禁忌
                personalFoodProhibitionTv.text = data.dietTaboo
            }
        }
    }

    override fun initEvent() {
        mBinding.run {
            personalDetailsBackIv.setOnClickListener {
                finish()
            }
            personalRealAvatarContainer.setOnClickListener {
                choosePhoto()
            }
            personalNameContainer.setOnClickListener {
                // 跳转到姓名设置界面
                PersonalNameSettingActivity.startActivity(
                    this@PersonalDetailsActivity,
                    personalNameTv.text.toString()
                )
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
                PersonalIdCardSettingActivity.startActivity(
                    this@PersonalDetailsActivity,
                    personalIdentityNumberTv.text.toString()
                )
            }
            personalBirthdayContainer.setOnClickListener {
                val startCalendar = Calendar.getInstance().apply {
                    set(1820, 1, 1)
                }
                val endCalendar = Calendar.getInstance()
                // 生日日期的选择
                TimePickerBuilder(this@PersonalDetailsActivity) { date, _ ->
                    val format = RxTimeTool.simpleDateFormat("yyyy年MM月dd日", date)
                    modifyParams("birthday", format)
                    // 计算出生日期
                    val sdf = SimpleDateFormat("yyyy年MM月dd", Locale.getDefault())
                    val bornDate = sdf.parse(format)
                    if (bornDate != null) {
                        // 计算年龄
                        val ageParams = mutableMapOf<String, String>()
                        ageParams["age"] = getAgeByBirth(bornDate).toString()
                        Repository.modifyPersonBasicInfo(
                            lifecycle = lifecycle,
                            params = ageParams
                        ) {
                            loadData()
                        }
                    }
                }
                    .isCyclic(true) // 是否循环滚动
                    .setDate(endCalendar)
                    .setRangDate(startCalendar, endCalendar) //起始终止年月日设定
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
            personalHomeAddressContainer.setOnClickListener {
                // 户籍地址选择
                AddressDialog.Builder(this@PersonalDetailsActivity)
                    //.setTitle("选择地区")
                    // 设置默认省份
                    .setProvince(mProvince)
                    // 设置默认城市（必须要先设置默认省份）
                    .setCity(mCity)
                    // 不选择县级区域
                    //.setIgnoreArea()
                    .setListener { _, province, city, area ->
                        val address = province + city + area
                        if (personalHomeAddressTv.text != address) {
                            mProvince = province
                            mCity = city
                            mArea = area
                            modifyParams("permanent_address", address)
                        }
                    }
                    .show()
            }
            personalPhoneNumberContainer.setOnClickListener {
                // 联系电话的手机号码设置
                PersonalPhoneSettingActivity.startActivity(
                    this@PersonalDetailsActivity,
                    personalPhoneNumberTv.text.toString()
                )
            }
            personalHobbyContainer.setOnClickListener {
                // 爱好选择
                val hobbyList = mutableListOf<String>().apply {
                    add("象棋")
                    add("羽毛球")
                    add("围棋")
                    add("乒乓球")
                    add("其它")
                }
                OptionsPickerBuilder(this@PersonalDetailsActivity) { options1: Int, _: Int, _: Int, _: View? ->
                    val hobby = hobbyList[options1]
                    modifyParams("hobby", hobby)
                }
                    .isAlphaGradient(true)
                    .build<String>()
                    .apply {
                        setOnDismissListener {
                            hobbyList.clear()
                        }
                        setPicker(hobbyList)
                        show()
                    }
            }
            personalFoodProhibitionContainer.setOnClickListener {
                // 饮食禁忌填写
                startActivity<PersonalFoodProhibitionSettingActivity>()
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

    /**
     * 进入相册图片选择界面，获取用户选择的照片或拍摄的照片
     */
    @SuppressLint("InlinedApi")
    private fun choosePhoto() {
        var allPermissionGranted = false
        // 相机权限和读写文件权限是必要的，否则用户将无法正常使用更换头像功能
        XXPermissions.with(this)
            .permission(
                arrayOf(
                    Manifest.permission.CAMERA, Manifest.permission.MANAGE_EXTERNAL_STORAGE
                )
            )
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                    allPermissionGranted = all
                }

                override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                    simpleToast("权限申请被拒绝，请手动授予相机和存储权限！")
                    XXPermissions.startPermissionActivity(
                        this@PersonalDetailsActivity,
                        permissions
                    )
                }
            })
        // 如果没有同时授予相机、存储权限，该功能将无法正常使用
        if (allPermissionGranted.not()) return
        Matisse.from(this)
            .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
            .countable(false)
            .maxSelectable(1)
            .gridExpectedSize(SizeUtils.dp2px(100f))
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .thumbnailScale(0.85f)
            .imageEngine(GlideEngine())
            .showPreview(false) // Default is `true`
            .capture(true)
            .maxOriginalSize(5)
            .captureStrategy(
                CaptureStrategy(
                    false,
                    "${AppConfig.getPackageName()}.provider",
                    "Paradise"
                )
            )
            .forResult(REQUEST_CODE_CHOOSE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
        if (requestCode == REQUEST_CODE_CHOOSE) {
            val selected: List<Uri> = Matisse.obtainResult(data)
            if (selected.isNullOrEmpty()) return
            copyUriToExternalFilesDir(
                selected[0],
                "Temp_${System.currentTimeMillis()}.jpg"
            ) { newFile ->
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("正在上传图片..")
                progressDialog.setCancelable(false)
                progressDialog.show()
                // Log.d(TAG, "onActivityResult: ===>File：${it.path}")
                Repository.uploadImageFile(newFile, lifecycle = lifecycle, onFinished = {
                    progressDialog.dismiss()
                }) { uploadImage ->
                    val newUserAvatar = uploadImage.data.imgUrl
                    Log.d(TAG, "onActivityResult: ===>newUserAvatar：$newUserAvatar")
                    val params = mutableMapOf<String, String>()
                    params["img"] = newUserAvatar
                    Repository.modifyPersonBasicInfo(
                        lifecycle = lifecycle,
                        params = params,
                    ) {
                        // 修改成功之后，刷新界面数据
                        Glide.with(this)
                            .load(newUserAvatar)
                            .into(mBinding.personalRealAvatarIv)
                        simpleToast(it.msg)
                    }
                }
            }
            // Log.d(TAG, "mSelected：====>$selected")
        }
    }

    companion object {
        private const val TAG = "PersonalDetailsActivity"
        private const val REQUEST_CODE_CHOOSE = 1
    }
}