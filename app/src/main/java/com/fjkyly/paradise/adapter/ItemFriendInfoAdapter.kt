package com.fjkyly.paradise.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fjkyly.paradise.R
import com.fjkyly.paradise.expand.inflate
import com.fjkyly.paradise.model.FriendsList

class ItemFriendInfoAdapter : RecyclerView.Adapter<ItemFriendInfoAdapter.InnerHolder>() {

    private val mFriendsListData = arrayListOf<FriendsList.Data>()
    private lateinit var mListener: (friendsListData: FriendsList.Data, position: Int) -> Unit

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemFriendInfoAdapter.InnerHolder {
        val itemView = inflate(R.layout.item_friend_info, parent)
        return InnerHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemFriendInfoAdapter.InnerHolder, position: Int) {
        val friendsListData = mFriendsListData[position]
        holder.run {
            friendNameTv.text = friendsListData.fullName
            friendRelationTv.text = friendsListData.andRelation
            friendPhoneNumTv.text = friendsListData.phone
            friendEditorTv.setOnClickListener {
                if (::mListener.isInitialized) mListener(friendsListData, position)
            }
        }
    }

    fun setOnItemClickListener(listener: (friendsListData: FriendsList.Data, position: Int) -> Unit) {
        mListener = listener
    }

    override fun getItemCount(): Int = mFriendsListData.size

    fun setData(data: MutableList<FriendsList.Data>) {
        mFriendsListData.run {
            clear()
            addAll(data)
            notifyDataSetChanged()
        }
    }

    inner class InnerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val friendNameTv: TextView = itemView.findViewById(R.id.friendNameTv)
        val friendRelationTv: TextView = itemView.findViewById(R.id.friendRelationTv)
        val friendPhoneNumTv: TextView = itemView.findViewById(R.id.friendPhoneNumTv)
        val friendEditorTv: TextView = itemView.findViewById(R.id.friendEditorTv)
    }
}