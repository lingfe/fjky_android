package com.fjkyly.paradise.ui.fragment

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.fjkyly.paradise.R
import com.fjkyly.paradise.base.BaseFragment
import com.fjkyly.paradise.databinding.FragmentMeBinding
import com.fjkyly.paradise.expand.startActivity
import com.fjkyly.paradise.network.request.Repository
import com.fjkyly.paradise.ui.activity.AccountManagerActivity
import com.fjkyly.paradise.ui.activity.FeedbackSettingActivity
import com.fjkyly.paradise.ui.activity.FriendsListActivity
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

            }
            meMessageIv.setOnClickListener {

            }
            meInclude.run {
                Repository.queryUserBasicInfo(lifecycle = lifecycle) {
                    val data = it.data
                    val userInfo = data.userInfo
                    Glide.with(this@MeFragment)
                        .load(userInfo.userImg)
                        .into(meAvatarIv)
                    meAccountIdTv.text = userInfo.username
                }
                mePhoneModifyContainer.setOnClickListener {
                    // 进入账号管理界面
                    requireContext().startActivity<AccountManagerActivity>()
                }
                meFavoritesContainer.setOnClickListener {
                    // 我的余额被点击
                }
                meAccountContainer.setOnClickListener {
                    // 电话号码被点击的事件
                }
            }
            mePersonDetailInfoContainer.setOnClickListener {
                // 进入到个人详细信息界面
                requireContext().startActivity<PersonalDetailsActivity>()
            }
            meRelativesContainer.setOnClickListener {
                // 进入亲友列表界面
                requireContext().startActivity<FriendsListActivity>()
            }
            meFeedbackContainer.setOnClickListener {
                // 进入意见反馈界面
                requireContext().startActivity<FeedbackSettingActivity>()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}