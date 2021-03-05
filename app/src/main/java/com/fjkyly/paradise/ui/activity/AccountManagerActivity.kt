package com.fjkyly.paradise.ui.activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.Glide
import com.fjkyly.paradise.R
import com.fjkyly.paradise.base.App
import com.fjkyly.paradise.base.BaseActivity
import com.fjkyly.paradise.databinding.ActivityAccountManagerBinding
import com.fjkyly.paradise.expand.AppConfig
import com.fjkyly.paradise.expand.copyUriToExternalFilesDir
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.expand.startActivity
import com.fjkyly.paradise.network.request.Repository
import com.fjkyly.paradise.ui.views.ConfirmDialog
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.vondear.rxtool.RxActivityTool
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy

/**
 * 账户管理界面
 *
 * @property mBinding ActivityAccountManagerBinding
 */
@RequiresApi(Build.VERSION_CODES.R)
class AccountManagerActivity : BaseActivity() {

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
            val oldUserAvatar = App.getUserAvatar()
            // Log.d(TAG, "loadData: ===>oldUserAvatar：$oldUserAvatar")
            Glide.with(this@AccountManagerActivity)
                .load(oldUserAvatar)
                .error(R.drawable.icon_person)
                .into(accountMangerAvatarIv)
            App.accountLoginInfo?.run {
                val userInfo = data.userInfo
                val assInfo = data.assInfo
                Log.d(TAG, "loadData: ===>${userInfo.username}")
                accountMangerNameTv.text = userInfo.username
                accountMangerPhoneNumTv.text = assInfo.phone
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
                // TODO: 2021/2/25 弹出对话框，选择头像的来源（相册或拍摄）
                // if (::mChoosePhotoDialog.isInitialized.not()) {
                //     mChoosePhotoDialog = Dialog(this@AccountManagerActivity)
                //     val dialogBinding =
                //         DialogChoosePhotoBinding.inflate(mChoosePhotoDialog.layoutInflater)
                //     mChoosePhotoDialog.setContentView(dialogBinding.root)
                //     dialogBinding.run {
                //         dialogAlbumChooseTv.setOnClickListener {
                //             // TODO: 2021/3/1 打开相册界面，并获取用户选择的图片数据（暂未实现获取图片数据）
                //             // selectAlbum()
                //             choosePhoto()
                //             simpleToast("从相册获取图片正在开发...")
                //         }
                //         dialogTakePhotoTv.setOnClickListener {
                //             // TODO: 2021/3/1 打开相机界面，并获取用户拍照后的图片数据（暂未实现获取图片数据）
                //             // openCamera()
                //             simpleToast("拍摄图片正在开发...")
                //         }
                //         dialogCancelTv.setOnClickListener {
                //             mChoosePhotoDialog.dismiss()
                //         }
                //     }
                // }
                // mChoosePhotoDialog.show()
                // simpleToast("更换头像正在开发中...")
            }
            accountMangerNameContainer.setOnClickListener {
                // 跳转到用户名设置界面
                startActivity<UserNameSettingActivity>()
            }
            accountMangerPhoneNumContainer.setOnClickListener {
                // TODO: 2021/2/25 跳转到修改登录手机号界面
                simpleToast("修改登录手机号正在开发中...")
            }
            accountMangerPwdContainer.setOnClickListener {
                // TODO: 2021/2/25 跳转到账号密码修改界面
                simpleToast("账号密码管理功能正在开发中...")
            }
            accountMangerPayContainer.setOnClickListener {
                // TODO: 2021/2/25 支付密码管理界面
                simpleToast("支付密码管理功能正在开发中...")
            }
            accountMangerLoginOutTv.setOnClickListener {
                // TODO: 2021/2/25 退出登录
                val signOutDialog = ConfirmDialog(this@AccountManagerActivity)
                signOutDialog.run {
                    setContentView(R.layout.dialog_confirm)
                    post {
                        setDialogMessage("确定要退出登录吗")
                        setConfirmTextColor(Color.parseColor("#666666"))
                        setGivUpTextColor(Color.parseColor("#FF5050"))
                    }
                    show()
                }
                signOutDialog.setOnDialogActionClickListener(object :
                    ConfirmDialog.OnDialogActionSimpleListener() {
                    override fun onGiveUpClick() {
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
                Repository.uploadImageFile(newFile, lifecycle = lifecycle) { uploadImage ->
                    val newUserAvatar = uploadImage.data.imgUrl
                    // Log.d(TAG, "onActivityResult: ===>newUserAvatar：$newUserAvatar")
                    Repository.modifyUserAvatar(
                        newUserAvatar = newUserAvatar,
                        lifecycle = lifecycle
                    ) {
                        Glide.with(this)
                            .load(newUserAvatar)
                            .error(R.drawable.icon_person)
                            .into(mBinding.accountMangerAvatarIv)
                        simpleToast("头像修改成功！")
                        newFile.delete()
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