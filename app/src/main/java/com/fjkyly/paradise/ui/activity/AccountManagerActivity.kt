package com.fjkyly.paradise.ui.activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.edit
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.Glide
import com.fjkyly.paradise.R
import com.fjkyly.paradise.base.App
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.databinding.ActivityAccountManagerBinding
import com.fjkyly.paradise.expand.*
import com.fjkyly.paradise.network.request.Repository
import com.fjkyly.paradise.ui.views.ConfirmDialog
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.vondear.rxtool.RxActivityTool
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy

@RequiresApi(Build.VERSION_CODES.R)
class AccountManagerActivity : MyActivity() {

    private lateinit var mBinding: ActivityAccountManagerBinding
    private lateinit var mChoosePhotoDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAccountManagerBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        mBinding.run {
            Repository.queryUserInfo(lifecycle = lifecycle) {
                if (it == null) {
                    return@queryUserInfo
                }
                val data = it.data
                kotlin.runCatching {
                    val userImg = data.userImg
                    // 用户头像
                    Glide.with(this@AccountManagerActivity)
                        .load(userImg)
                        .into(accountMangerAvatarIv)
                }
                kotlin.runCatching {
                    val userName = data.username
                    // 用户名称
                    accountMangerNameTv.text = userName
                }
                kotlin.runCatching {
                    val userPhone = data.phone
                    // 用户手机号
                    accountMangerPhoneNumTv.text = userPhone
                }
            }
        }
    }

    override fun initEvent() {
        mBinding.run {
            accountMangerBackIv.setOnClickListener {
                finish()
            }
            accountMangerAvatarContainer.setOnClickListener {
                choosePhoto()
            }
            accountMangerNameContainer.setOnClickListener {
                // 跳转到用户名设置界面
                UserNameSettingActivity.startActivity(
                    this@AccountManagerActivity,
                    accountMangerNameTv.text.toString()
                )
            }
            accountMangerPhoneNumContainer.setOnClickListener {
                // 跳转到修改登录手机号界面
                startActivity<BindNewPhoneActivity>()
            }
            accountMangerPwdContainer.setOnClickListener {
                // 跳转到账号密码修改界面
                ModifyPwdActivity.startActivity(
                    this@AccountManagerActivity,
                    accountMangerPhoneNumTv.text.toString()
                )
            }
            accountMangerPayContainer.setOnClickListener {

            }
            accountSettingContainer.setOnClickListener {
                // 跳转到APP设置界面
                startActivity<AppSettingActivity>()
            }
            accountMangerLoginOutTv.setOnClickListener {
                // 退出登录
                val signOutDialog = ConfirmDialog(this@AccountManagerActivity)
                signOutDialog.run {
                    setContentView(R.layout.dialog_confirm)
                    post {
                        setDialogMessage("确定要退出登录吗？")
                        setConfirmTextColor(Color.parseColor("#666666"))
                        setGivUpTextColor(Color.parseColor("#FF5050"))
                    }
                    show()
                }
                signOutDialog.setOnDialogActionClickListener(object :
                    ConfirmDialog.OnDialogActionSimpleListener() {
                    override fun onGiveUpClick() {
                        // 将自动登录状态设置为 false
                        getSharedPreferences(
                            USER_SETTING,
                            Context.MODE_PRIVATE
                        ).edit(commit = true) {
                            putBoolean(AUTO_LOGIN_STATUS, false)
                        }
                        Repository.signOut(lifecycle = lifecycle) {
                            // 账号退出成功的回调
                            RxActivityTool.skipActivityAndFinishAll(
                                this@AccountManagerActivity,
                                LoginActivity::class.java
                            )
                            // 退出登录之后，需要将用户信息置空
                            App.accountLoginInfo = null
                        }
                    }
                })
            }
        }
    }

    /**
     * 进入相册图片选择界面，获取用户选择的照片或拍摄的照片
     */
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
                        this@AccountManagerActivity,
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
                // Log.d(TAG, "onActivityResult: ===>File：${it.path}")
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("正在上传图片..")
                progressDialog.setCancelable(false)
                progressDialog.show()
                Repository.uploadImageFile(newFile, lifecycle = lifecycle, onFinished = {
                    progressDialog.dismiss()
                }) { uploadImage ->
                    val newUserAvatar = uploadImage.data.imgUrl
                    Log.d(TAG, "onActivityResult: ===>newUserAvatar：$newUserAvatar")
                    Repository.modifyUserAvatar(
                        newUserAvatar = newUserAvatar,
                        lifecycle = lifecycle
                    ) { modifyUserAvatar ->
                        progressDialog.dismiss()
                        Glide.with(this)
                            .load(newUserAvatar)
                            .error(R.drawable.icon_person)
                            .into(mBinding.accountMangerAvatarIv)
                        simpleToast(modifyUserAvatar.msg)
                    }
                }
            }
            // Log.d(TAG, "mSelected：====>$selected")
        }
    }

    companion object {
        private const val TAG = "AccountManagerActivity"
        private const val REQUEST_CODE_CHOOSE = 1
    }
}