package com.fjkyly.paradise.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutResId(), container, false)
    }

    abstract fun getLayoutResId(): Int

    open fun callAllInit() {
        initView()
        initData()
        initEvent()
        initPresenter()
    }

    open fun initView() {

    }

    open fun initData() {

    }

    open fun initEvent() {

    }

    open fun initPresenter() {

    }

    open fun onBackPress(): Boolean = false
}
