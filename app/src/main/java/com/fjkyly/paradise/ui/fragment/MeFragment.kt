package com.fjkyly.paradise.ui.fragment

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.fjkyly.paradise.R
import com.fjkyly.paradise.base.App
import com.fjkyly.paradise.base.BaseFragment
import com.fjkyly.paradise.databinding.FragmentMeBinding
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.expand.startActivity
import com.fjkyly.paradise.ui.activity.AccountManagerActivity

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
                simpleToast("设置功能开发中...")
            }
            meMessageIv.setOnClickListener {
                simpleToast("消息功能开发中...")
            }
            meInclude.run {
                Glide.with(this@MeFragment)
                    .load(App.getUserAvatar())
                    .into(meAvatarIv)
                meAccountIdTv.text = App.getUserName()
                mePhoneModifyContainer.setOnClickListener {
                    // TODO: 2021-03-05 进入账号管理界面
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
                // requireContext().startActivity<PersonalDetailsActivity>()
                simpleToast("个人详细信息开发中...")
            }
            meRelativesContainer.setOnClickListener {
                // TODO: 2021-03-05 进入亲友列表界面
                simpleToast("亲友功能开发中...")
            }
            meFeedbackContainer.setOnClickListener {
                // TODO: 2021-03-05 进入意见反馈界面
                simpleToast("意见反馈功能开发中...")
            }
            meCommonProblemContainer.setOnClickListener {
                // TODO: 2021-03-05 进入常见问题查看界面
                simpleToast("常见问题功能开发中...")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}