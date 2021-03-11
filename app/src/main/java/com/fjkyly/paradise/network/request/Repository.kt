package com.fjkyly.paradise.network.request

import android.util.Log
import androidx.lifecycle.Lifecycle
import com.blankj.utilcode.util.GsonUtils
import com.fjkyly.paradise.base.App
import com.fjkyly.paradise.expand.fileToMultipartBodyParts
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.model.*
import com.fjkyly.paradise.network.request.api.*
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response
import java.io.File

object Repository {

    /**
     * 获取用户基本信息（包含:用户名、头像、基本信息等）
     */
    fun queryUserBasicInfo(lifecycle: Lifecycle, block: (userBasicInfo: UserBasicInfo) -> Unit) {
        val queryUserBasicInfoApi = ServiceCreator.create<QueryUserBasicInfoApi>()
        queryUserBasicInfoApi.query()
            .enqueue(object : retrofit2.Callback<UserBasicInfo> {
                override fun onResponse(
                    call: Call<UserBasicInfo>,
                    response: Response<UserBasicInfo>
                ) {
                    val body = response.body()?.let {
                        Log.d(TAG, "onResponse：queryUserBasicInfo ===>${GsonUtils.toJson(it)}")
                        if (it.state == HTTP_OK) {
                            if (lifecycle.currentState != Lifecycle.State.DESTROYED) {
                                block(it)
                            }
                        } else {
                            simpleToast(it.msg)
                        }
                    }
                    if (body == null) {
                        simpleToast("服务器数据异常，请稍后重试！")
                    }
                }

                override fun onFailure(call: Call<UserBasicInfo>, t: Throwable) {
                    t.printStackTrace()
                    simpleToast("用户信息获取失败，请稍后重试！")
                }
            })
    }

    fun unbindDevice(
        deviceId: String, lifecycle: Lifecycle,
        block: (unbindDevice: UnbindDevice) -> Unit
    ) {
        val unbindDeviceApi = ServiceCreator.create<UnbindDeviceApi>()
        unbindDeviceApi.unbind(deviceId = deviceId)
            .enqueue(object : retrofit2.Callback<UnbindDevice> {
                override fun onResponse(
                    call: Call<UnbindDevice>,
                    response: Response<UnbindDevice>
                ) {
                    val body = response.body()?.let {
                        Log.d(TAG, "onResponse：unbindDevice ===>${GsonUtils.toJson(it)}")
                        if (it.state == HTTP_OK) {
                            if (lifecycle.currentState != Lifecycle.State.DESTROYED) {
                                block(it)
                            }
                        } else {
                            simpleToast(it.msg)
                        }
                    }
                    if (body == null) {
                        simpleToast("服务器数据异常，请稍后重试！")
                    }
                }

                override fun onFailure(call: Call<UnbindDevice>, t: Throwable) {
                    t.printStackTrace()
                    simpleToast("设备解绑失败，请稍后重试！")
                }
            })
    }

    /**
     * 查询设备最新的位置
     */
    fun queryDeviceNewestLocation(
        lifecycle: Lifecycle,
        block: (deviceNewestLocation: DeviceNewestLocation) -> Unit
    ) {
        val queryDeviceNewestLocationApi = ServiceCreator.create<QueryDeviceNewestLocationApi>()
        queryDeviceNewestLocationApi.query()
            .enqueue(object : retrofit2.Callback<DeviceNewestLocation> {
                override fun onResponse(
                    call: Call<DeviceNewestLocation>,
                    response: Response<DeviceNewestLocation>
                ) {
                    val body = response.body()?.let {
                        // Log.d(TAG, "onResponse：queryDeviceNewestLocation ===>${GsonUtils.toJson(it)}")
                        if (it.state == HTTP_OK) {
                            if (lifecycle.currentState != Lifecycle.State.DESTROYED) {
                                block(it)
                            }
                        } else {
                            simpleToast(it.msg)
                        }
                    }
                    if (body == null) {
                        simpleToast("服务器数据异常，请稍后重试！")
                    }
                }

                override fun onFailure(call: Call<DeviceNewestLocation>, t: Throwable) {
                    t.printStackTrace()
                    simpleToast("设备位置获取失败，请稍后重试！")
                }
            })
    }

    /**
     * 查询用户设备列表
     */
    fun queryBindDeviceList(lifecycle: Lifecycle, block: (bindDeviceList: BindDeviceList) -> Unit) {
        val deviceListApi = ServiceCreator.create<QueryBindDeviceListApi>()
        deviceListApi.query()
            .enqueue(object : retrofit2.Callback<BindDeviceList> {
                override fun onResponse(
                    call: Call<BindDeviceList>,
                    response: Response<BindDeviceList>
                ) {
                    val body = response.body()?.let {
                        Log.d(TAG, "onResponse：queryBindDeviceList ===>${GsonUtils.toJson(it)}")
                        if (it.state == HTTP_OK) {
                            if (lifecycle.currentState != Lifecycle.State.DESTROYED) {
                                block(it)
                            }
                        } else {
                            simpleToast(it.msg)
                        }
                    }
                    if (body == null) {
                        simpleToast("服务器数据异常，请稍后重试！")
                    }
                }

                override fun onFailure(call: Call<BindDeviceList>, t: Throwable) {
                    t.printStackTrace()
                    simpleToast("设备列表获取失败，请稍后重试！")
                }
            })
    }

    /**
     * 退出账号
     */
    fun signOut(lifecycle: Lifecycle, block: () -> Unit) {
        val signOutApi = ServiceCreator.create<SignOutApi>()
        signOutApi.singOut()
            .enqueue(object : retrofit2.Callback<SignOut> {
                override fun onResponse(call: Call<SignOut>, response: Response<SignOut>) {
                    val body = response.body()?.let {
                        Log.d(TAG, "onResponse：signOut ===>${GsonUtils.toJson(it)}")
                        if (it.state == HTTP_OK) {
                            if (lifecycle.currentState != Lifecycle.State.DESTROYED) {
                                block()
                            }
                        } else {
                            simpleToast(it.msg)
                        }
                    }
                    if (body == null) {
                        simpleToast("服务器数据异常，请稍后重试！")
                    }
                }

                override fun onFailure(call: Call<SignOut>, t: Throwable) {
                    t.printStackTrace()
                    simpleToast("退出登录失败，请稍后重试！")
                }
            })
    }

    /**
     * 通过设备 id 查询设备信息
     *
     * @param deviceId String
     * @param lifecycle Lifecycle
     * @param block Function1<[@kotlin.ParameterName] DeviceInfo, Unit>
     */
    fun queryDeviceInfo(
        deviceId: String,
        lifecycle: Lifecycle,
        block: (deviceInfo: DeviceInfo) -> Unit
    ) {
        val queryDeviceInfoApi = ServiceCreator.create<QueryDeviceInfoApi>()
        queryDeviceInfoApi.query(deviceId = deviceId)
            .enqueue(object : retrofit2.Callback<DeviceInfo> {
                override fun onResponse(call: Call<DeviceInfo>, response: Response<DeviceInfo>) {
                    val body = response.body()?.let {
                        Log.d(TAG, "onResponse：queryDeviceInfo ===>${GsonUtils.toJson(it)}")
                        if (it.state == HTTP_OK) {
                            if (lifecycle.currentState != Lifecycle.State.DESTROYED) {
                                block(it)
                            }
                        } else {
                            simpleToast(it.msg)
                        }
                    }
                    if (body == null) {
                        simpleToast("服务器数据异常，请稍后重试！")
                    }
                }

                override fun onFailure(call: Call<DeviceInfo>, t: Throwable) {
                    t.printStackTrace()
                    simpleToast("设备信息查询失败，请稍后重试！")
                }
            })
    }

    /**
     * 通过设备 id 绑定设备
     *
     * @param deviceId String 设备 id
     * @param lifecycle Lifecycle
     * @param block Function1<[@kotlin.ParameterName] BindDevice, Unit>
     */
    fun bindDevice(
        deviceId: String,
        lifecycle: Lifecycle,
        block: (bindDevice: BindDevice) -> Unit
    ) {
        val bindDeviceApi = ServiceCreator.create<BindDeviceApi>()
        bindDeviceApi.binding(deviceId = deviceId)
            .enqueue(object : retrofit2.Callback<BindDevice> {
                override fun onResponse(call: Call<BindDevice>, response: Response<BindDevice>) {
                    val body = response.body()?.let {
                        Log.d(TAG, "onResponse：bindDevice ===>${Gson().toJson(it)}")
                        if (it.state == HTTP_OK) {
                            if (lifecycle.currentState != Lifecycle.State.DESTROYED) {
                                block(it)
                            }
                        } else {
                            simpleToast(it.msg)
                        }
                    }
                    if (body == null) {
                        simpleToast("服务器数据异常，请稍后重试！")
                    }
                }

                override fun onFailure(call: Call<BindDevice>, t: Throwable) {
                    t.printStackTrace()
                    simpleToast("设备绑定失败，请稍后重试！")
                }
            })
    }

    /**
     * 上传图片文件
     *
     * @param file File
     * @param block Function0<Unit>
     */
    fun uploadImageFile(
        file: File,
        lifecycle: Lifecycle,
        block: (uploadImage: UploadImage) -> Unit
    ) {
        val uploadFileApi = ServiceCreator.create<UploadFileApi>()
        val multipartBodyPart = fileToMultipartBodyParts(file)
        uploadFileApi.upload(multipartBodyPart = multipartBodyPart)
            .enqueue(object : retrofit2.Callback<UploadImage> {
                override fun onResponse(
                    call: Call<UploadImage>,
                    response: Response<UploadImage>
                ) {
                    val body = response.body()?.let {
                        Log.d(TAG, "onResponse：uploadImageFile ===>${GsonUtils.toJson(it)}")
                        if (it.state == HTTP_OK) {
                            if (lifecycle.currentState != Lifecycle.State.DESTROYED) {
                                block(it)
                            }
                        } else {
                            simpleToast(it.msg)
                        }
                    }
                    if (body == null) {
                        simpleToast("服务器数据异常，请稍后重试！")
                    }
                }

                override fun onFailure(call: Call<UploadImage>, t: Throwable) {
                    t.printStackTrace()
                    simpleToast("图片上传失败，请稍后重试！")
                }
            })
    }

    /**
     * 修改用户头像
     *
     * @param newUserAvatar String 新的用户头像地址
     * @param lifecycle Lifecycle
     * @param block Function0<Unit>
     */
    fun modifyUserAvatar(
        newUserAvatar: String,
        lifecycle: Lifecycle,
        block: (modifyUserAvatar: ModifyUserAvatar) -> Unit
    ) {
        val modifyUserAvatarApi = ServiceCreator.create<ModifyUserAvatarApi>()
        modifyUserAvatarApi.modify(newUserAvatar = newUserAvatar)
            .enqueue(object : retrofit2.Callback<ModifyUserAvatar> {
                override fun onResponse(
                    call: Call<ModifyUserAvatar>,
                    response: Response<ModifyUserAvatar>
                ) {
                    val body = response.body()?.let {
                        Log.d(TAG, "onResponse：modifyUserAvatar ===>${GsonUtils.toJson(it)}")
                        if (it.state == HTTP_OK) {
                            if (lifecycle.currentState != Lifecycle.State.DESTROYED) {
                                block(it)
                            }
                        } else {
                            simpleToast(it.msg)
                        }
                    }
                    if (body == null) {
                        simpleToast("服务器数据异常，请稍后重试！")
                    }
                }

                override fun onFailure(call: Call<ModifyUserAvatar>, t: Throwable) {
                    t.printStackTrace()
                    simpleToast("用户头像修改失败，请稍后重试！")
                }
            })
    }

    /**
     * 修改用户名
     *
     * @param newUserName String 新用户名（2-12位即可）
     * @param lifecycle Lifecycle
     * @param block Function0<Unit>
     */
    fun modifyUserName(
        newUserName: String,
        lifecycle: Lifecycle,
        block: (modifyUserName: ModifyUserName) -> Unit
    ) {
        val modifyUserNameApi = ServiceCreator.create<ModifyUserNameApi>()
        modifyUserNameApi.modify(userName = newUserName)
            .enqueue(object : retrofit2.Callback<ModifyUserName> {
                override fun onResponse(
                    call: Call<ModifyUserName>,
                    response: Response<ModifyUserName>
                ) {
                    val body = response.body()?.let {
                        Log.d(TAG, "onResponse：modifyUserName ===>${GsonUtils.toJson(it)}")
                        if (it.state == HTTP_OK) {
                            if (lifecycle.currentState != Lifecycle.State.DESTROYED) {
                                block(it)
                            }
                        } else {
                            simpleToast(it.msg)
                        }
                    }
                    if (body == null) {
                        simpleToast("服务器数据异常，请稍后重试！")
                    }
                }

                override fun onFailure(call: Call<ModifyUserName>, t: Throwable) {
                    t.printStackTrace()
                    simpleToast("用户名修改失败，请稍后重试！")
                }
            })
    }

    /**
     * 登录账号
     *
     * @param accountNum String 账号名称（手机号/身份证号码）
     * @param accountPwd String 密码
     * @param block Function0<Unit>
     */
    fun loginAccount(
        accountNum: String,
        accountPwd: String,
        lifecycle: Lifecycle,
        block: () -> Unit
    ) {
        val accountLoginApi = ServiceCreator.create<AccountLoginApi>()
        accountLoginApi.login(accountNum = accountNum, accountPwd = accountPwd).enqueue(object :
            retrofit2.Callback<Login> {
            override fun onResponse(call: Call<Login>, response: Response<Login>) {
                val body = response.body()?.let {
                    if (it.state == HTTP_OK) {
                        App.accountLoginInfo = it
                        if (lifecycle.currentState != Lifecycle.State.DESTROYED) {
                            block()
                        }
                    } else {
                        simpleToast(it.msg)
                    }
                }
                if (body == null) {
                    simpleToast("服务器数据异常，请稍后重试！")
                }
            }

            override fun onFailure(call: Call<Login>, t: Throwable) {
                t.printStackTrace()
                simpleToast("登录失败，请稍后重试！")
            }
        })
    }

    /**
     * 注册用户账号
     *
     * @param phoneNum String 手机号
     * @param pwd String 密码
     * @param idCard String 身份证号码
     */
    fun registerAccount(
        phoneNum: String,
        pwd: String,
        idCard: String,
        lifecycle: Lifecycle,
        block: (register: Register) -> Unit
    ) {
        val registerApi = ServiceCreator.create<AccountRegisterApi>()
        registerApi.register(phoneNum = phoneNum, pwd = pwd, idCard = idCard)
            .enqueue(object : retrofit2.Callback<Register> {
                override fun onResponse(
                    call: Call<Register>,
                    response: Response<Register>
                ) {
                    val body = response.body()?.let {
                        if (it.state == HTTP_OK) {
                            if (lifecycle.currentState != Lifecycle.State.DESTROYED) {
                                block(it)
                            }
                        } else {
                            simpleToast(it.msg)
                        }
                    }
                    if (body == null) {
                        simpleToast("服务器数据异常，请检查后重试！")
                    }
                }

                override fun onFailure(call: Call<Register>, t: Throwable) {
                    t.printStackTrace()
                    simpleToast("注册失败，请检查后重试！")
                }
            })
    }

    private const val TAG = "Repository"
}