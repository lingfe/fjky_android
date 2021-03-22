package com.fjkyly.paradise.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ActivityUtils
import com.fjkyly.paradise.R
import com.fjkyly.paradise.adapter.ItemDeviceFunSettingAdapter
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.databinding.ActivityDeviceFunSettingBinding
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.model.DeviceSettingFun
import com.fjkyly.paradise.model.Facility
import com.fjkyly.paradise.network.request.Repository
import com.fjkyly.paradise.ui.views.ConfirmDialog

class DeviceFunSettingActivity : MyActivity() {

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
            deviceFunSettingBackIv.setOnClickListener {
                finish()
            }
            unBindFacilityBtn.setOnClickListener {
                // 用户点击了解绑设备按钮
                val unBindFacilityDialog = ConfirmDialog(this@DeviceFunSettingActivity)
                unBindFacilityDialog.run {
                    setContentView(R.layout.dialog_confirm)
                    post {
                        setDialogMessage("确定解绑该设备吗？")
                        setConfirmTextColor(Color.parseColor("#666666"))
                        setGivUpTextColor(Color.parseColor("#FF5050"))
                    }
                    show()
                }
                unBindFacilityDialog.setOnDialogActionClickListener(object :
                    ConfirmDialog.OnDialogActionSimpleListener() {
                    override fun onGiveUpClick() {
                        // 用户点击了确定按钮，解绑设备
                        Repository.unbindDevice(
                            deviceId = mFacility.facilityId,
                            lifecycle = lifecycle
                        ) {
                            finish()
                            simpleToast(it.msg)
                        }
                    }
                })
            }
        }
        itemDeviceFunSettingAdapter.setOnItemClickListener { deviceFun, _ ->
            when (deviceFun.funValueType) {
                // 输入值的界面
                0 -> InputValueActivity.startActivity(this, deviceFun = deviceFun)
                3 -> InputValueActivity.startActivity(this, deviceFun = deviceFun)
                5 -> InputValueActivity.startActivity(this, deviceFun = deviceFun)
                6 -> InputValueActivity.startActivity(this, deviceFun = deviceFun)
                7 -> InputValueActivity.startActivity(this, deviceFun = deviceFun)
                8 -> InputValueActivity.startActivity(this, deviceFun = deviceFun)
                // 区间值选择界面
                1 -> VolumeSettingActivity.startActivity(this, deviceFun = deviceFun)
                // 开关界面
                2 -> ToggleActivity.startActivity(this, deviceFun = deviceFun)
                10 -> ToggleActivity.startActivity(this, deviceFun = deviceFun)
                // 日期范围选择界面
                4 -> DateRangeActivity.startActivity(this, deviceFun = deviceFun)
                // 无参接口，直接调用
                9 -> executeInstructions(deviceFun)
                // 功能类型不在定义的范围，提示用户不支持该功能
                else -> simpleToast("该版本不支持此功能")
            }
        }
    }

    /**
     * 直接执行指令
     */
    private fun executeInstructions(deviceFun: DeviceSettingFun.Data.DevFun) {
        simpleToast("指令已发送")
        // 调用后端接口，修改功能参数数据
        Repository.modifyDeviceFunValue(
            deviceFunId = deviceFun.id,
            deviceFunValue = true.toString(),
            lifecycle = lifecycle
        ) {
            // 指令发送成功
            simpleToast(it.msg)
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