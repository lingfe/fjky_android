package com.fjkyly.paradise.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.fjkyly.paradise.adapter.ItemBindFacilityAdapter
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.databinding.ActivityAddFacilityBinding
import com.fjkyly.paradise.expand.QR_SCAN_CONTENT
import com.fjkyly.paradise.expand.QR_SCAN_REQUEST_CODE
import com.fjkyly.paradise.expand.fromJson
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.model.DeviceQrInfo
import com.fjkyly.paradise.model.Facility
import com.fjkyly.paradise.network.request.Repository

class AddFacilityActivity : MyActivity() {

    private var mDeviceId = ""
    private lateinit var mBinding: ActivityAddFacilityBinding
    private val itemBindFacilityAdapter by lazy {
        ItemBindFacilityAdapter()
    }
    private val mFacilityList = arrayListOf<Facility>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddFacilityBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initView() {
        mBinding.run {
            addFacilityRv.run {
                layoutManager = LinearLayoutManager(this@AddFacilityActivity)
                adapter = itemBindFacilityAdapter
            }
        }
    }

    private fun loadData() {
        Repository.queryDeviceInfo(deviceId = mDeviceId, lifecycle = lifecycle) {
            val deviceData = it.data
            val deviceName = deviceData.dname
            val deviceId = deviceData.id
            // 设备类型
            val deviceType = deviceData.dtypeId.toIntOrNull() ?: -1
            val deviceStatus = deviceData.state
            val userId = deviceData.userId
            Log.d(TAG, "loadData: ===>userId：$userId")
            Log.d(
                TAG,
                "onQueryTextSubmit: ===>deviceData：${GsonUtils.toJson(deviceData)}"
            )
            mFacilityList.run {
                clear()
                val facility = Facility(
                    name = deviceName,
                    facilityId = deviceId,
                    facilityType = deviceType,
                    facilityStatus = deviceStatus,
                    facilityBinding = isEmpty(userId).not()
                )
                add(facility)
            }
            itemBindFacilityAdapter.setData(mFacilityList)
            mBinding.noDataContainerLL.visibility =
                if (mFacilityList.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun initEvent() {
        mBinding.run {
            addFacilityBackIv.setOnClickListener {
                finish()
            }
            addFacilitySv.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    // 通过设备 id 查询设备信息
                    if (query.isNullOrEmpty()) {
                        simpleToast("请输入设备ID！")
                        return false
                    }
                    mDeviceId = query
                    loadData()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    // simpleToast("搜索建议功能正在开发中")
                    return false
                }
            })
            addFacilityScanIv.setOnClickListener {
                // 跳转到扫码界面，并获取扫码结果
                startActivityForResult(
                    Intent(
                        this@AddFacilityActivity,
                        ScanQrCodeActivity::class.java
                    ), QR_SCAN_REQUEST_CODE
                )
            }
        }
        itemBindFacilityAdapter.setOnItemClickListener { facility, _ ->
            Repository.bindDevice(deviceId = facility.facilityId, lifecycle = lifecycle) {
                loadData()
                simpleToast(it.msg)
            }
        }
    }

    private fun isEmpty(text: String) = TextUtils.isEmpty(text) || "null" == text.trim()

    override fun onDestroy() {
        super.onDestroy()
        mFacilityList.clear()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == QR_SCAN_REQUEST_CODE && data != null) {
                val qrContent = data.getStringExtra(QR_SCAN_CONTENT)
                if (qrContent.isNullOrBlank()) {
                    return
                }
                runCatching {
                    val deviceQrInfo: DeviceQrInfo = fromJson(qrContent)
                    val deviceData = deviceQrInfo.data
                    val deviceId = deviceData.devId
                    if (deviceId.isEmpty().not()) {
                        mBinding.addFacilitySv.setQuery(deviceId, true)
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "AddFacilityActivity"
    }
}