package com.fjkyly.paradise.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.blankj.utilcode.util.ActivityUtils
import com.fjkyly.paradise.base.BaseActivity
import com.fjkyly.paradise.databinding.ActivityDateRangeBinding
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.model.DeviceSettingFun
import com.fjkyly.paradise.network.request.Repository
import com.vondear.rxtool.RxTimeTool
import java.text.SimpleDateFormat
import java.util.*

/**
 * 通用的日期范围选择界面
 */
class DateRangeActivity : BaseActivity() {

    private var _binding: ActivityDateRangeBinding? = null
    private val mBinding get() = _binding!!
    private var startTime = System.currentTimeMillis()
    private var endTime = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDateRangeBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initView() {
        mBinding.run {
            dateRangeTitleTv.text = mDeviceFun.funName
            updateTime()
        }
    }

    private fun ActivityDateRangeBinding.updateTime() {
        startDate.text = RxTimeTool.getDate((startTime / 1000L).toString(), "yyyy-MM- dd")
        endDate.text = RxTimeTool.getDate((endTime / 1000L).toString(), "yyyy-MM-dd")
    }

    override fun initEvent() {
        mBinding.run {
            startDate.setOnClickListener {
                TimePickerBuilder(this@DateRangeActivity) { date: Date?, _: View? ->
                    date ?: return@TimePickerBuilder
                    startTime = date.time
                    updateTime()
                }.build().show()
            }
            endDate.setOnClickListener {
                TimePickerBuilder(this@DateRangeActivity) { date: Date?, _: View? ->
                    date ?: return@TimePickerBuilder
                    endTime = date.time
                    updateTime()
                }.build().show()
            }
            saveBtn.setOnClickListener {
                if (startTime > endTime) {
                    simpleToast("开始日期不能大于结束日期")
                    return@setOnClickListener
                }
                // 调用后端接口，修改功能参数数据
                Repository.modifyDeviceFunValue(
                    deviceFunId = mDeviceFun.id,
                    // 时间范围，用 ~ 隔开，如：2021.01.01~2022.01.01
                    deviceFunValue = "${sdf.format(startTime)}~${sdf.format(endTime)}",
                    lifecycle = lifecycle
                ) {
                    // 指令发送成功
                    simpleToast(it.msg)
                }
            }
        }
    }

    companion object {
        private const val TAG = "DateRangeActivity"
        private val sdf = SimpleDateFormat("yyyy.MM.dd", Locale.SIMPLIFIED_CHINESE)
        private lateinit var mDeviceFun: DeviceSettingFun.Data.DevFun

        fun startActivity(context: Context, deviceFun: DeviceSettingFun.Data.DevFun) {
            mDeviceFun = deviceFun
            ActivityUtils.startActivity(Intent(context, DateRangeActivity::class.java))
        }
    }
}