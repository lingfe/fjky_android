package com.fjkyly.paradise.ui.fragment

import android.os.Bundle
import android.view.View
import com.fjkyly.paradise.R
import com.fjkyly.paradise.adapter.ItemHealthDataAdapter
import com.fjkyly.paradise.base.BaseFragment
import com.fjkyly.paradise.databinding.FragmentHealthDataBinding
import com.fjkyly.paradise.model.HealthMenuAction

/**
 * 健康数据界面
 *
 * @property _binding FragmentHealthDataBinding?
 * @property mBinding FragmentHealthDataBinding
 * @property itemHealthDataAdapter ItemHealthDataAdapter
 * @property menuActionList MutableList<MenuAction>
 */
class HealthDataFragment : BaseFragment() {

    private var _binding: FragmentHealthDataBinding? = null
    private val mBinding get() = _binding!!
    private val itemHealthDataAdapter by lazy {
        ItemHealthDataAdapter()
    }
    private val menuActionList = arrayListOf<HealthMenuAction>()

    override fun getLayoutResId(): Int = R.layout.fragment_health_data

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentHealthDataBinding.bind(view)
        callAllInit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun initView() {
        mBinding.run {
            // healthDataMenuRv.run {
            //     adapter = itemHealthDataAdapter
            //     layoutManager = GridLayoutManager(requireContext(), 4)
            // }
        }
    }

    override fun initData() {
        // menuActionList.run {
        //     add(MenuAction(R.drawable.icon_jiankang, "健康档案"))
        //     add(MenuAction(R.drawable.icon_baogao, "健康报告"))
        //     add(MenuAction(R.drawable.icon_yujing, "预警设置"))
        //     add(MenuAction(R.drawable.icon_naozhong, "吃药提醒"))
        //     add(MenuAction(R.drawable.icon_anquan, "安全中心"))
        //     add(MenuAction(R.drawable.icon_pinggu, "评估中心"))
        //     add(MenuAction(R.drawable.icon_yiyuan, "合作医院"))
        //     add(MenuAction(R.drawable.icon_yisheng, "家庭医生"))
        //     itemHealthDataAdapter.setData(this)
        // }
    }

    override fun initEvent() {
        // itemHealthDataAdapter.setOnItemClickListener { menuAction, position ->
        //     // TODO: 2021/2/22 跳转到指定功能菜单对应的界面
        //     simpleToast("跳转到${menuAction.menuName}界面")
        // }
    }

    companion object {
        private const val TAG = "HealthDataFragment"
    }
}