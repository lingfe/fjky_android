package com.fjkyly.paradise.ui.activity

import android.os.Bundle
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.databinding.ActivityFriendsListBinding

class FriendsListActivity : MyActivity() {

    private var _binding: ActivityFriendsListBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFriendsListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initEvent() {
        mBinding.run {
            friendsListBackIv.setOnClickListener {
                finish()
            }
            addFriendTv.setOnClickListener {
                // TODO: 2021-03-18 跳转到添加亲友界面

            }
        }
    }
}