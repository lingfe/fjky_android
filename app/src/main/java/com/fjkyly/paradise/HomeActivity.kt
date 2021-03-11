package com.fjkyly.paradise

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.fjkyly.paradise.base.BaseActivity
import com.fjkyly.paradise.base.BaseFragment
import com.fjkyly.paradise.base.BaseFragmentStateAdapter
import com.fjkyly.paradise.databinding.ActivityHomeBinding
import com.fjkyly.paradise.ui.fragment.BrowserFragment
import com.fjkyly.paradise.ui.fragment.CalendarEventTestFragment
import com.fjkyly.paradise.ui.fragment.FacilityFragment
import com.fjkyly.paradise.ui.fragment.MeFragment
import com.fjkyly.paradise.ui.views.NavigationMediator

/**
 * 主界面
 *
 * @property mBinding ActivityHomeBinding
 * @property fragmentStateAdapter BaseFragmentStateAdapter
 * @property fragmentList MutableList<BaseFragment>
 * @property navigationMediator NavigationMediator
 */
class HomeActivity : BaseActivity() {

    private lateinit var mBinding: ActivityHomeBinding
    private val fragmentStateAdapter by lazy {
        BaseFragmentStateAdapter(this)
    }
    private val fragmentList = arrayListOf<BaseFragment>()
    private lateinit var navigationMediator: NavigationMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initView() {
        mBinding.run {
            homeViewPager2.run {
                adapter = fragmentStateAdapter
                isUserInputEnabled = false
            }
        }
    }

    override fun initData() {
        fragmentList.run {
            add(BrowserFragment())
            add(FacilityFragment())
            add(CalendarEventTestFragment().setEmptyTips("商城"))
            add(CalendarEventTestFragment().setEmptyTips("资讯"))
            add(MeFragment())
        }
        fragmentStateAdapter.setFragmentList(fragmentList)
    }

    override fun initEvent() {
        mBinding.run {
            navigationMediator = NavigationMediator(
                homeBottomView,
                homeViewPager2,
                false
            )
            navigationMediator.attach()
            homeBottomView.setOnNavigationItemSelectedListener { item: MenuItem? ->
                val index: Int = navigationMediator.getIndexByItem(item)
                homeViewPager2.setCurrentItem(index, false)
                true
            }
            //优化底部导航栏长按时有提示的问题
            val menu: Menu = homeBottomView.menu
            for (i in 0 until menu.size()) {
                val item = menu.getItem(i)
                homeBottomView.findViewById<View>(item.itemId).setOnLongClickListener { true }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::navigationMediator.isInitialized) navigationMediator.detach()
    }

    companion object {
        private const val TAG = "HomeActivity"
    }
}