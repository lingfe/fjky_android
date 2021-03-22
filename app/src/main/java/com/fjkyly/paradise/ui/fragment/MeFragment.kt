package com.fjkyly.paradise.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.fjkyly.paradise.R
import com.fjkyly.paradise.base.BaseFragment
import com.fjkyly.paradise.databinding.FragmentMeBinding
import com.fjkyly.paradise.expand.hidePhoneNum
import com.fjkyly.paradise.expand.startActivity
import com.fjkyly.paradise.network.request.Repository
import com.fjkyly.paradise.ui.activity.*
import kotlinx.coroutines.launch

/**
 * 我的 界面
 *
 * @property _binding FragmentMeBinding?
 * @property mBinding FragmentMeBinding
 */
class MeFragment : BaseFragment() {

    private var _binding: FragmentMeBinding? = null
    private val mBinding get() = _binding!!
    private var avatarUrl: String? = null

    override fun getLayoutResId(): Int = R.layout.fragment_me

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentMeBinding.bind(view)
        callAllInit()
    }

    override fun initView() {
        mBinding.run {
            meSettingsIv.setOnClickListener {
                // TODO: 2021-03-19 设置按钮
            }
            meMessageIv.setOnClickListener {
                // TODO: 2021-03-19 消息按钮
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        mBinding.run {
            meInclude.run {
                Repository.queryUserInfo(lifecycle = lifecycle) {
                    if (it == null) {
                        return@queryUserInfo
                    }
                    val data = it.data
                    lifecycleScope.launch {
                        runCatching {
                            if (TextUtils.isEmpty(data.userImg).not()) {
                                avatarUrl = data.userImg
                            }
                            Glide.with(this@MeFragment)
                                .load(avatarUrl)
                                .into(meAvatarIv)
                        }
                        // meAvatarIv.setImageDrawable(
                        //     RoundImageDrawable(
                        //         bitmap.getOrThrow(),
                        //         SizeUtils.dp2px(0f).toFloat()
                        //     )
                        // )
                    }
                    runCatching {
                        meAccountIdTv.text = data.username
                    }
                    runCatching {
                        val balance = data.balance
                        meFavoritesNumTv.text = balance.toString()
                    }
                    // 隐藏手机号中间四位
                    runCatching {
                        val phone = hidePhoneNum(data.phone)
                        meAccountTv.text = phone
                    }
                }
            }
        }
    }

    override fun initEvent() {
        mBinding.run {
            meInclude.run {
                meAvatarIv.setOnClickListener {
                    ViewBitPhotoActivity.startActivity(
                        this@MeFragment.requireContext(),
                        avatarUrl = avatarUrl
                    )
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