package com.fjkyly.paradise.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ActivityUtils
import com.fjkyly.paradise.adapter.ItemFacilityListAdapter
import com.fjkyly.paradise.base.BaseActivity
import com.fjkyly.paradise.databinding.ActivitySmartBandsListBinding
import com.fjkyly.paradise.model.Facility
import com.fjkyly.paradise.network.request.Repository

/**
 * 智能手环列表界面
 */
class SmartBandsListActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySmartBandsListBinding
    private val itemFacilityListAdapter by lazy {
        ItemFacilityListAdapter(facilityBrandName = true)
    }
    private val mFacilityList = arrayListOf<Facility>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySmartBandsListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initView() {
        mBinding.run {
            smartBandsBackIv.setOnClickListener {
                finish()
            }
            smartBandsNameTv.text = mFacility.name
            smartBandsListRv.run {
                layoutManager = LinearLayoutManager(this@SmartBandsListActivity)
                adapter = itemFacilityListAdapter
            }
        }
    }

    override fun initEvent() {
        itemFacilityListAdapter.setOnItemClickListener { facility, _ ->
            SmartBandsActivity.startActivity(this, facility = facility)
        }
    }

    override fun onResume() {
        super.onResume()
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

    override fun onDestroy() {
        super.onDestroy()
        mFacilityList.clear()
    }

    companion object {
        private lateinit var mFacility: Facility

        fun startActivity(context: Context, facility: Facility) {
            mFacility = facility
            ActivityUtils.startActivity(Intent(context, SmartBandsListActivity::class.java))
        }
    }
}