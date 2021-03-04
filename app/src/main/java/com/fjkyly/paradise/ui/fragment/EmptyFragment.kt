package com.fjkyly.paradise.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.fjkyly.paradise.R
import com.fjkyly.paradise.base.BaseFragment
import com.fjkyly.paradise.databinding.FragmentEmptyBinding

class EmptyFragment : BaseFragment() {

    private var _binding: FragmentEmptyBinding? = null
    private val mBinding get() = _binding!!
    var mEmptyTips = "空的"

    fun setEmptyTips(text: String): EmptyFragment {
        mEmptyTips = text
        return this
    }

    override fun getLayoutResId(): Int = R.layout.fragment_empty

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentEmptyBinding.bind(view)
        callAllInit()
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        mBinding.run {
            emptyTipsTv.text = "${mEmptyTips}测试界面"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}