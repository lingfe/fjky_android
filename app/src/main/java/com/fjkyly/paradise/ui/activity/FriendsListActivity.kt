package com.fjkyly.paradise.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.fjkyly.paradise.adapter.ItemFriendInfoAdapter
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.databinding.ActivityFriendsListBinding
import com.fjkyly.paradise.model.FriendsList
import com.fjkyly.paradise.network.request.Repository

class FriendsListActivity : MyActivity() {

    private var _binding: ActivityFriendsListBinding? = null
    private val mBinding get() = _binding!!
    private val itemFriendInfoAdapter = ItemFriendInfoAdapter()
    private var mFriendsListData = mutableListOf<FriendsList.Data>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFriendsListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initView() {
        mBinding.run {
            friendsListRv.run {
                layoutManager = LinearLayoutManager(this@FriendsListActivity)
                adapter = itemFriendInfoAdapter
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        Repository.queryFriendsList(lifecycle = lifecycle) {
            mFriendsListData.run {
                clear()
                addAll(it.data)
                itemFriendInfoAdapter.setData(this)
            }
            mBinding.noFriendsContainer.visibility =
                if (mFriendsListData.isNotEmpty()) View.GONE else View.VISIBLE
        }
    }

    override fun initEvent() {
        mBinding.run {
            friendsListBackIv.setOnClickListener {
                finish()
            }
            addFriendTv.setOnClickListener {
                // 跳转到添加亲友界面
                AddFriendActivity.startActivity(
                    this@FriendsListActivity,
                    isAdd = true
                )
            }
            itemFriendInfoAdapter.setOnItemClickListener { friendsListData, _ ->
                // 跳转到拨号界面，用户拨打亲友号码
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + friendsListData.phone))
                startActivity(intent)
            }
            itemFriendInfoAdapter.setOnEditorClickListener { friendsListData, _ ->
                // 修改亲友信息
                AddFriendActivity.startActivity(
                    context = this@FriendsListActivity,
                    friendName = friendsListData.fullName,
                    friendPhoneNum = friendsListData.phone,
                    friendRelation = friendsListData.andRelation,
                    friendId = friendsListData.id,
                    isAdd = false
                )
            }
        }
    }
}