package com.fjkyly.paradise.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fjkyly.paradise.R
import com.fjkyly.paradise.expand.inflate
import com.fjkyly.paradise.model.Facility

class ItemFacilityListAdapter : RecyclerView.Adapter<ItemFacilityListAdapter.InnerHolder>() {

    private val mFacilityList = mutableListOf<Facility>()
    private lateinit var mListener: (facility: Facility, position: Int) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val itemView = inflate(R.layout.item_facility, parent)
        return InnerHolder(itemView)
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val facility = mFacilityList[position]
        holder.run {
            itemView.setOnClickListener {
                if (::mListener.isInitialized) mListener(facility, position)
            }
            Glide.with(itemView).load(facility.getFacilityIcon()).into(facilityIconIv)
            facilityNameTv.text = facility.name
            facilityStatusTv.text = facility.getFacilityStatus()
        }
    }

    fun setOnItemClickListener(listener: (facility: Facility, position: Int) -> Unit) {
        mListener = listener
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
    }
}