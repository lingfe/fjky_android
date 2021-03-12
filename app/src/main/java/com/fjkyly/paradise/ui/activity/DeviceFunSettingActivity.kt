package com.fjkyly.paradise.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ActivityUtils
import com.fjkyly.paradise.adapter.ItemDeviceFunSettingAdapter
import com.fjkyly.paradise.base.BaseActivity
import com.fjkyly.paradise.databinding.ActivityDeviceFunSettingBinding
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.model.DeviceSettingFun
import com.fjkyly.paradise.model.Facility
import com.fjkyly.paradise.network.request.Repository

/**
 * 设备功能列表界面
 *
 */
class DeviceFunSettingActivity : BaseActivity() {

    private lateinit var mBinding: ActivityDeviceFunSettingBinding
    private val itemDeviceFunSettingAdapter by lazy {
        ItemDeviceFunSettingAdapter()
    }
    private val mDeviceFunList = arrayListOf<DeviceSettingFun.Data.DevFun>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityDeviceFunSettingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initView() {
        mBinding.run {
            facilityNameTv.text = mFacility.facilityBrandName
            facilityIdTv.text = mFacility.facilityId
            smartBandsMenuRv.layoutManager = LinearLayoutManager(this@DeviceFunSettingActivity)
            smartBandsMenuRv.adapter = itemDeviceFunSettingAdapter
        }
    }

    override fun initData() {
        Repository.queryDeviceFunList(
            deviceId = mFacility.facilityId,
            lifecycle = lifecycle
        ) {
            mDeviceFunList.apply {
                clear()
                addAll(it.data.devFunList)
                // 根据功能 ID 进行排序
                sortedBy { it.funId }
            }.also { deviceSettingFunList ->
                itemDeviceFunSettingAdapter.setData(deviceSettingFunList)
            }
        }
    }

    override fun initEvent() {
        mBinding.run {
            smartBandsSettingBackIv.setOnClickListener {
                finish()
            }
            modifyFacilityNameTv.setOnClickListener {
                simpleToast("修改设备名称正在开发中...")
            }
            editorPermissionTv.setOnClickListener {
                simpleToast("编辑亲友查看权限正在开发中...")
            }
            unBindFacilityBtn.setOnClickListener {
                Repository.unbindDevice(deviceId = mFacility.facilityId, lifecycle = lifecycle) {
                    finish()
                    simpleToast("设备解绑成功！")
                }
            }
        }
        itemDeviceFunSettingAdapter.setOnItemClickListener { deviceFun, _ ->
            when (deviceFun.funValueType) {
                "1" -> RangeRulerActivity.startActivity(this, deviceFun = deviceFun)
            }
            simpleToast("功能名称：${deviceFun.funName}")
        }
    }

    companion object {
        private lateinit var mFacility: Facility

        fun startActivity(context: Context, facility: Facility) {
            mFacility = facility
            ActivityUtils.startActivity(Intent(context, DeviceFunSettingActivity::class.java))
        }
    }
}