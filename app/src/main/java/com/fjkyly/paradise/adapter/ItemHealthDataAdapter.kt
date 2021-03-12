package com.fjkyly.paradise.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fjkyly.paradise.R
import com.fjkyly.paradise.expand.inflate
import com.fjkyly.paradise.model.HealthMenuAction
import de.hdodenhof.circleimageview.CircleImageView

class ItemHealthDataAdapter : RecyclerView.Adapter<ItemHealthDataAdapter.InnerHolder>() {

    private val mMenuList = arrayListOf<HealthMenuAction>()
    private lateinit var mListener: (healthMenuAction: HealthMenuAction, position: Int) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val itemView = inflate(R.layout.item_health_data_menu, parent)
        return InnerHolder(itemView)
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val menuAction = mMenuList[position]
        holder.run {
            itemView.setOnClickListener {
                if (::mListener.isInitialized) {
                    mListener(menuAction, position)
                }
            }
            Glide.with(itemView).load(menuAction.icon).into(menuIconIv)
            menuTv.text = menuAction.menuName
        }
    }

    fun setOnItemClickListener(listener: (healthMenuAction: HealthMenuAction, position: Int) -> Unit) {
        mListener = listener
    }

    override fun getItemCount(): Int = mMenuList.size

    fun setData(data: MutableList<HealthMenuAction>) {
        mMenuList.run {
            clear()
            addAll(data)
            notifyDataSetChanged()
        }
    }

    inner class InnerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val menuIconIv: CircleImageView = itemView.findViewById(R.id.menuIconIv)
        val menuTv: TextView = itemView.findViewById(R.id.menuTv)
    }
}