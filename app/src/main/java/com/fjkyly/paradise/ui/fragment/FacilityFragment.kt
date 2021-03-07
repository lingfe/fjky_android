package com.fjkyly.paradise.ui.fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.blankj.utilcode.util.GsonUtils
import com.fjkyly.paradise.R
import com.fjkyly.paradise.adapter.CustomInfoWindowAdapter
import com.fjkyly.paradise.adapter.ItemFacilityListAdapter
import com.fjkyly.paradise.base.BaseFragment
import com.fjkyly.paradise.databinding.FragmentFacilityBinding
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.expand.startActivity
import com.fjkyly.paradise.model.Facility
import com.fjkyly.paradise.network.request.Repository
import com.fjkyly.paradise.ui.activity.AddFacilityActivity
import com.fjkyly.paradise.ui.activity.SmartBandsSettingActivity
import com.vondear.rxtool.RxAppTool


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
    private val mMapAppList = arrayListOf("com.autonavi.minimap", "com.baidu.BaiduMap")

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

    override fun onResume() {
        super.onResume()
        Repository.queryDeviceNewestLocation(lifecycle = lifecycle) { deviceNewestLocation ->
            val deviceNewestLocationData = deviceNewestLocation.data
            val latitude =
                deviceNewestLocationData.latitude.toDoubleOrNull() ?: 39.908689
            val longitude =
                deviceNewestLocationData.longitude.toDoubleOrNull() ?: 116.397475
            val address = deviceNewestLocationData.address
            val latLng = LatLng(latitude, longitude)
            initMapInfoWindow(latLng, address)
        }
        mBinding.facilityMapView.onResume()
        loadData()
    }

    private fun loadData() {
        Repository.queryBindDeviceList(lifecycle = lifecycle) {
            mFacilityList.run {
                clear()
                val facilityList = it.data
                for (data in facilityList) {
                    mFacilityList.add(
                        Facility(
                            name = data.dtypeName,
                            facilityBrandName = data.dname,
                            facilityId = data.id,
                            facilityStatus = data.state,
                            facilityType = 0
                        )
                    )
                }
                itemFacilityListAdapter.setData(this)
            }
        }
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
        Log.d(TAG, "initMapInfoWindow: ===>latLng：${GsonUtils.toJson(latLng)}")
        val markerOptions = MarkerOptions()
            .position(latLng)
            .title("设备位置")
            .snippet(address)
            .icon(
                BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.icon_dingwei
                    )
                )
            )
            .draggable(true)
            .setFlat(false)
        val marker = mAMap.addMarker(markerOptions)
        mAMap.setInfoWindowAdapter(CustomInfoWindowAdapter())
        marker.showInfoWindow()
        mAMap.setOnInfoWindowClickListener {
            // 获取所有已安装App信息
            val appInfoList = RxAppTool.getAllAppsInfo(requireContext())
            Log.d(
                TAG,
                "initMapInfoWindow: ===>${if (appInfoList.isEmpty()) "无法获取到已安装的APP列表" else "已安装APP列表获取成功"}"
            )
            val installAppPackageList = arrayListOf<String>()
            for (appInfo in appInfoList) {
                installAppPackageList.add(appInfo.packageName)
            }
            val intersectList = installAppPackageList.intersect(mMapAppList)
            for (packageName in intersectList) {
                Log.d(TAG, "initMapInfoWindow: ===>packageName：$packageName")
            }
            if (intersectList.isEmpty()) {
                simpleToast("请先安装第三方导航APP")
                return@setOnInfoWindowClickListener
            }
            Log.d(
                TAG,
                "initMapInfoWindow: ===>${if (intersectList.isEmpty()) "没有安装导航APP" else "安装了导航APP"}"
            )
            //  打开手机上用户已经安装的导航APP进行导航
            val uri: Uri = Uri.parse("geo:${latLng.latitude},${latLng.longitude}")
            val openNavigationAPPIntent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(openNavigationAPPIntent)
            simpleToast("打开导航APP功能开发中...")
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

    override fun initEvent() {
        mBinding.run {
            addFacilityTv.setOnClickListener {
                requireContext().startActivity<AddFacilityActivity>()
            }
        }
        itemFacilityListAdapter.setOnItemClickListener { facility, _ ->
            // TODO: 2021-03-05 此处应该根据设备类型进行判断需要跳转到哪一个界面
            if (facility.facilityType == 0) {
                SmartBandsSettingActivity.startActivity(requireContext(), facility = facility)
            } else {
                simpleToast("点击了${facility.name}，状态${facility.getFacilityStatus()}，功能正在开发中...")
            }
        }
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