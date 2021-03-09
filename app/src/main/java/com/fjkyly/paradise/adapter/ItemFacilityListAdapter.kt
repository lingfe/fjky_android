package com.fjkyly.paradise.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fjkyly.paradise.R
import com.fjkyly.paradise.expand.inflate
import com.fjkyly.paradise.model.Facility

class ItemFacilityListAdapter(val facilityBrandName: Boolean = false) :
    RecyclerView.Adapter<ItemFacilityListAdapter.InnerHolder>() {

    private val mFacilityList = arrayListOf<Facility>()
    private lateinit var mItemClickListener: (facility: Facility, position: Int) -> Unit
    private lateinit var mSettingClickListener: (facility: Facility, position: Int) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val itemView = inflate(R.layout.item_facility, parent)
        return InnerHolder(itemView)
    }

    private var lastTime = 0L

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val facility = mFacilityList[position]
        holder.run {
            itemView.setOnClickListener {
                if (::mItemClickListener.isInitialized) {
                    val currentTime = System.currentTimeMillis()
                    val singleClick =
                        currentTime - lastTime > ViewConfiguration.getDoubleTapTimeout()
                    // 如果只有用户的手指是单击时才回调，防止用户连续点击
                    if (singleClick) mItemClickListener(facility, position)
                    lastTime = currentTime
                }
            }
            Glide.with(itemView).load(facility.getFacilityIcon()).into(facilityIconIv)
            facilityNameTv.text =
                if (facilityBrandName.not()) facility.name else facility.facilityBrandName
            facilitySettingBtn.setOnClickListener {
                if (::mSettingClickListener.isInitialized) {
                    val currentTime = System.currentTimeMillis()
                    val singleClick =
                        currentTime - lastTime > ViewConfiguration.getDoubleTapTimeout()
                    // 如果只有用户的手指是单击时才回调，防止用户连续点击
                    if (singleClick) mSettingClickListener(facility, position)
                    lastTime = currentTime
                }
            }
            facilityStatusTv.text = facility.getFacilityStatus()
            // 如果是正常状态则为绿色字体，否则为红色字体
            facilityStatusTv.setTextColor(if (facility.facilityStatus == 1) Color.RED else Color.RED)
            facilityTypeNameTv.text = "--${facility.facilityTypeName}"
        }
    }

    fun setOnItemClickListener(listener: (facility: Facility, position: Int) -> Unit) {
        mItemClickListener = listener
    }

    fun setOnSettingClickListener(listener: (facility: Facility, position: Int) -> Unit) {
        mSettingClickListener = listener
    }

    override fun getItemCount(): Int = mFacilityList.size

    fun setData(data: MutableList<Facility>) {
        mFacilityList.run {
            clear()
            addAll(data)
            notifyDataSetChanged()
        }
    }

    inner class InnerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val facilityIconIv: ImageView = itemView.findViewById(R.id.facilityIconIv)
        val facilityNameTv: TextView = itemView.findViewById(R.id.facilityNameTv)
        val facilityStatusTv: TextView = itemView.findViewById(R.id.facilityStatusTv)
        val facilitySettingBtn: Button = itemView.findViewById(R.id.facilitySettingBtn)
        val facilityTypeNameTv: TextView = itemView.findViewById(R.id.facilityTypeNameTv)
    }
}