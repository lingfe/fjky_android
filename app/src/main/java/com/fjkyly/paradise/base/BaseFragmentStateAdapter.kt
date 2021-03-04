package com.fjkyly.paradise.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class BaseFragmentStateAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val mFragmentList = arrayListOf<Fragment>()

    fun setFragmentList(fragmentList: MutableList<BaseFragment>) {
        mFragmentList.run {
            clear()
            addAll(fragmentList)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = mFragmentList.size

    override fun createFragment(position: Int) = mFragmentList[position]
}