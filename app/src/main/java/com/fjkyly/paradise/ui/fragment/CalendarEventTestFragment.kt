package com.fjkyly.paradise.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.fjkyly.paradise.R
import com.fjkyly.paradise.base.BaseFragment
import com.fjkyly.paradise.databinding.FragmentCalendarEventTestBinding
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.other.CalendarUtils
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 日历测试界面
 */
class CalendarEventTestFragment : BaseFragment(), OnPermissionCallback {

    private var _binding: FragmentCalendarEventTestBinding? = null
    private val mBinding get() = _binding!!
    var mEmptyTips = "空的"

    private var granted = false

    fun setEmptyTips(text: String): CalendarEventTestFragment {
        mEmptyTips = text
        return this
    }

    override fun getLayoutResId(): Int = R.layout.fragment_calendar_event_test

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentCalendarEventTestBinding.bind(view)
        callAllInit()
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        mBinding.run {
            emptyTipsTv.text = "${mEmptyTips}测试界面"
            insertCalendarEventBtn.setOnClickListener {
                checkCalendarPermission()
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        // TODO: 2021-03-09 测试日历添加、删除、查询功能，后期将从此处移除
                        CalendarUtils().apply {
                            setAlarmTime(0)
                            setRepeat(repeatRemindCb.isChecked)
                            setAlarm(clockRemindCb.isChecked)
                            val startTime = System.currentTimeMillis() + 10000
                            val endTime = startTime + 60000
                            insertCalendarEvent(
                                eventTitle = "吃药提醒",
                                eventDescription = "吃药药啦！",
                                eventLocation = "瑜龙世家",
                                startTimeMillis = startTime,
                                endTimeMillis = endTime
                            )
                        }
                        simpleToast("日历事件添加完毕！")
                    }
                }
            }
            checkCalendarPermissionBtn.setOnClickListener {
                checkCalendarPermission()
            }
            deleteCalendarEventBtn.setOnClickListener {
                checkCalendarPermission()
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        CalendarUtils().run {
                            deleteEventByTitle("吃药提醒") {}
                            simpleToast("日历事件删除完毕")
                        }
                    }
                }
            }
            queryCalendarEventBtn.setOnClickListener {
                checkCalendarPermission()
                lifecycleScope.launch {
                    CalendarUtils().run {
                        queryAllEvent {
                            resultInfoTv.text = it
                        }
                        simpleToast("日历事件查询完毕")
                    }
                }
            }
        }
    }

    private fun checkCalendarPermission() {
        XXPermissions.with(this)
            .permission(
                arrayOf(
                    Manifest.permission.READ_CALENDAR,
                    Manifest.permission.WRITE_CALENDAR
                )
            ).request(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
        granted = all
    }

    override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
        granted = false
    }
}