package com.fjkyly.paradise.ui.fragment

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.fjkyly.paradise.R
import com.fjkyly.paradise.adapter.CustomInfoWindowAdapter
import com.fjkyly.paradise.adapter.ItemFacilityListAdapter
import com.fjkyly.paradise.base.BaseFragment
import com.fjkyly.paradise.databinding.FragmentFacilityBinding
import com.fjkyly.paradise.expand.*
import com.fjkyly.paradise.model.Facility
import com.fjkyly.paradise.network.request.Repository
import com.fjkyly.paradise.other.CalendarUtils
import com.fjkyly.paradise.provider.LocationProvider
import com.fjkyly.paradise.ui.activity.AddFacilityActivity
import com.fjkyly.paradise.ui.activity.SmartBandsSettingActivity
import com.paul.eventreminder.model.CalendarEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 设备列表界面
 *
 * @property _binding FragmentFacilityBinding?
 * @property mBinding FragmentFacilityBinding
 * @property itemFacilityListAdapter ItemFacilityListAdapter
 * @property mFacilityList MutableList<Facility>
 */
class FacilityFragment : BaseFragment() {

    private var _binding: FragmentFacilityBinding? = null
    private val mBinding get() = _binding!!
    private val itemFacilityListAdapter by lazy {
        ItemFacilityListAdapter(facilityBrandName = true)
    }
    private val mFacilityList = mutableListOf<Facility>()
    private var _aMap: AMap? = null
    private val mAMap get() = _aMap!!
    private val mMapAppList = arrayListOf(GAO_DE_MAP, BAI_DU_MAP)

    override fun getLayoutResId(): Int = R.layout.fragment_facility

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentFacilityBinding.bind(view)
        mBinding.run {
            facilityMapView.run {
                onCreate(savedInstanceState)
                _aMap = facilityMapView.map
                mAMap.uiSettings.isZoomControlsEnabled = false
                mAMap.mapTextZIndex = 2
            }
        }
        callAllInit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mBinding.facilityMapView.onSaveInstanceState(outState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        Repository.queryDeviceNewestLocation(lifecycle = lifecycle) { deviceNewestLocation ->
            val deviceNewestLocationData = deviceNewestLocation.data
            val latitude =
                deviceNewestLocationData.latitude.toDoubleOrNull()
                    ?: LocationProvider.DEFAULT_LATITUDE
            val longitude =
                deviceNewestLocationData.longitude.toDoubleOrNull()
                    ?: LocationProvider.DEFAULT_LONGITUDE
            val address = deviceNewestLocationData.address
            val latLng = LatLng(latitude, longitude)
            initMapInfoWindow(latLng, address)
        }
        mBinding.facilityMapView.onResume()
        loadData()
        // TODO: 2021-03-09 仅为测试定时任务功能，后期将删除
        // requireContext().startService(Intent(requireContext(), RemindService::class.java))
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                // TODO: 2021-03-09 测试日历添加、删除、查询功能，后期将从此处移除
                // val calendarEventList = arrayListOf<CalendarEvent>()
                // for (week in 1..7) {
                //     val calendarEvent = CalendarEvent().apply {
                //         summary = "测试吃药提醒"
                //         content = "大郎，该吃药药啦~"
                //         loc = "瑜龙世家"
                //         startTime = "15:04"
                //         endTime = "15:05"
                //         dayOfWeek = week
                //         weekList = mutableListOf(0, 1, 2, 3, 5)
                //     }
                //     calendarEventList.add(calendarEvent)
                // }
                CalendarUtils().deleteCalendarEvent("测试吃药提醒")
                CalendarUtils()
                    .setAlarm(true)
                    .setAlarmTime(0)
                    .setRepeat(true).also {
                        val calendarEvent = CalendarEvent().apply {
                            summary = "测试吃药提醒"
                            content = "大郎，该吃药药啦~"
                            loc = "瑜龙世家"
                            startTime = "15:04"
                            endTime = "15:05"
                            dayOfWeek = 2
                            weekList = mutableListOf(0)
                        }
                        it.addCalendarEvent(calendarEvent, 0, null)
                    }
                // TODO: 2021-03-09 查询日历事件有点问题，游标被关闭了，待解决
                CalendarUtils().queryAllEvent()
            }
            simpleToast("日历事件添加完毕！")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadData() {
        Repository.queryBindDeviceList(lifecycle = lifecycle) {
            mFacilityList.run {
                clear()
                val facilityList = it.data
                for (data in facilityList) {
                    add(
                        Facility(
                            name = data.dtypeName,
                            facilityBrandName = data.dname,
                            facilityId = data.id,
                            facilityStatus = data.state,
                            facilityType = data.dtypeCode,
                            facilityTypeName = data.dtypeName
                        )
                    )
                }
                itemFacilityListAdapter.setData(this)
            }
        }
    }

    override fun initView() {
        mBinding.run {
            facilityRv.run {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = itemFacilityListAdapter
            }
        }
    }

    override fun initData() {
        val latLng = LatLng(
            39.908689,
            116.397475
        )
        initMapInfoWindow(latLng = latLng, address = "天安门")
    }

    /**
     * 初始化地图信息窗口
     */
    private fun initMapInfoWindow(latLng: LatLng, address: String) {
        // 参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
        val mCameraUpdate = CameraUpdateFactory.newCameraPosition(
            CameraPosition(latLng, 18f, 30f, 0f)
        )
        mAMap.moveCamera(mCameraUpdate)
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
        // Log.d(TAG, "initMapInfoWindow: ===>latLng：${GsonUtils.toJson(latLng)}")
        val markerOptions = MarkerOptions()
            // 在地图上标记位置的经纬度值。必填参数
            .position(latLng)
            // 点标记的标题
            .title("设备位置")
            // 点标记的内容
            .snippet(address)
            // 点标记的图标
            .icon(
                BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.icon_dingwei
                    )
                )
            )
            // 点标记是否可拖拽
            .draggable(true)
        val marker = mAMap.addMarker(markerOptions)
        mAMap.setInfoWindowAdapter(CustomInfoWindowAdapter())
        marker.showInfoWindow()
        mAMap.setOnInfoWindowClickListener {
            val installedMapApp = checkInstalledMapApp(mMapAppList)
            // 如果用户有安装第三方地图 APP ，则打开，否则提示用户安装
            if (installedMapApp) launchMapApp(
                requireContext(),
                latLng
            ) else simpleToast("请先安装第三方导航APP")
        }
    }

    override fun initEvent() {
        mBinding.run {
            addFacilityTv.setOnClickListener {
                requireContext().startActivity<AddFacilityActivity>()
            }
        }
        itemFacilityListAdapter.setOnSettingClickListener { facility, _ ->
            // 进入设备设置界面
            SmartBandsSettingActivity.startActivity(requireContext(), facility = facility)
        }
    }

    override fun onPause() {
        super.onPause()
        mBinding.facilityMapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.facilityMapView.onDestroy()
        mFacilityList.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mFacilityList.clear()
    }

    companion object {
        private const val TAG = "FacilityFragment"
    }
}