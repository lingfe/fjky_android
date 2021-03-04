package com.fjkyly.paradise.ui.fragment

import android.os.Bundle
import android.view.View
import com.fjkyly.paradise.R
import com.fjkyly.paradise.base.BaseFragment
import com.fjkyly.paradise.databinding.FragmentMeBinding
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.expand.startActivity
import com.fjkyly.paradise.ui.activity.AccountManagerActivity
import com.fjkyly.paradise.ui.activity.PersonalDetailsActivity

/**
 * 我的 界面
 *
 * @property _binding FragmentMeBinding?
 * @property mBinding FragmentMeBinding
 */
class MeFragment : BaseFragment() {

    private var _binding: FragmentMeBinding? = null
    private val mBinding get() = _binding!!

    override fun getLayoutResId(): Int = R.layout.fragment_me

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentMeBinding.bind(view)
        callAllInit()
    }

    override fun initView() {
        mBinding.run {
            meSettingsIv.setOnClickListener {
                simpleToast("设置")
            }
            meMessageIv.setOnClickListener {
                simpleToast("消息")
            }
            meInclude.run {
                mePhoneModifyContainer.setOnClickListener {
                    requireContext().startActivity<AccountManagerActivity>()
                }
                meFavoritesContainer.setOnClickListener {
                    // TODO: 2021/2/28 进入到我的收藏界面
                    simpleToast("收藏功能正在开发...")
                }
                meAccountContainer.setOnClickListener {
                    // TODO: 2021/2/28 进入到我的账户界面
                    simpleToast("账户功能正在开发中...")
                }
            }
            mePersonDetailInfoContainer.setOnClickListener {
                // TODO: 2021/2/28 进入到个人详细信息界面
                requireContext().startActivity<PersonalDetailsActivity>()
                simpleToast("个人详细信息")
            }
            meRelativesContainer.setOnClickListener {
                simpleToast("亲友")
            }
            meFeedbackContainer.setOnClickListener {
                simpleToast("意见反馈")
            }
            meCommonProblemContainer.setOnClickListener {
                simpleToast("常见问题")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}