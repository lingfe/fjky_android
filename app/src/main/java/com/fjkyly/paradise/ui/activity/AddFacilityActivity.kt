package com.fjkyly.paradise.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.fjkyly.paradise.adapter.ItemBindFacilityAdapter
import com.fjkyly.paradise.base.BaseActivity
import com.fjkyly.paradise.databinding.ActivityAddFacilityBinding
import com.fjkyly.paradise.expand.QR_SCAN_CONTENT
import com.fjkyly.paradise.expand.QR_SCAN_REQUEST_CODE
import com.fjkyly.paradise.expand.fromJson
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.model.DeviceQrInfo
import com.fjkyly.paradise.model.Facility
import com.fjkyly.paradise.network.request.Repository

/**
 * 添加设备界面
 *
 * @property mBinding ActivityAddFacilityBinding
 * @property itemBindFacilityAdapter ItemBindFacilityAdapter
 * @property mFacilityList MutableList<Facility>
 */
class AddFacilityActivity : BaseActivity() {

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

    override fun initData() {
        // TODO: 2021-03-04 为了测试方便，直接输入了测试数据
        mBinding.addFacilitySv.setQuery("626160002748438", true)
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
                    Repository.queryDeviceInfo(deviceId = query, lifecycle = lifecycle) {
                        val deviceData = it.data
                        val deviceName = deviceData.dname
                        val deviceId = deviceData.id
                        // 设备类型
                        val deviceType = deviceData.dtypeId.toIntOrNull() ?: -1
                        val deviceStatus = deviceData.state
                        mFacilityList.let { facilityList ->
                            facilityList.clear()
                            facilityList.add(
                                Facility(
                                    name = deviceName,
                                    facilityId = deviceId,
                                    facilityType = deviceType,
                                    facilityStatus = deviceStatus
                                )
                            )
                        }
                        itemBindFacilityAdapter.setData(mFacilityList)
                    }
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
                // TODO: 2021/3/3 此处设备的绑定有数据解析的问题，需要排查解决
                simpleToast("设备绑定成功！")
            }
        }
    }

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
                    mBinding.addFacilitySv.setQuery(deviceData.devId, true)
                }
            }
        }
    }
}