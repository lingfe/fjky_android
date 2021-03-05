package com.fjkyly.paradise.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.fjkyly.paradise.R
import com.fjkyly.paradise.adapter.ItemFacilityListAdapter
import com.fjkyly.paradise.base.BaseFragment
import com.fjkyly.paradise.databinding.FragmentFacilityBinding
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.expand.startActivity
import com.fjkyly.paradise.model.Facility
import com.fjkyly.paradise.ui.activity.AddFacilityActivity
import com.fjkyly.paradise.ui.activity.SmartBandsListActivity
import kotlin.random.Random

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
        ItemFacilityListAdapter()
    }
    private val mFacilityList = mutableListOf<Facility>()

    override fun getLayoutResId(): Int = R.layout.fragment_facility

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentFacilityBinding.bind(view)
        callAllInit()
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
        mFacilityList.run {
            clear()
            repeat(1) {
                add(
                    Facility(
                        name = "智能手环",
                        facilityId = "BNSJABJK2899172SNJ",
                        facilityType = it % 6,
                        facilityStatus = Random.nextInt(2)
                    )
                )
            }
            itemFacilityListAdapter.setData(this)
        }
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
                SmartBandsListActivity.startActivity(requireContext(), facility)
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
}