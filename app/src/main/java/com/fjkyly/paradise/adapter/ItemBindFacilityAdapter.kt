package com.fjkyly.paradise.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fjkyly.paradise.R
import com.fjkyly.paradise.expand.inflate
import com.fjkyly.paradise.expand.simpleToast
import com.fjkyly.paradise.model.Facility

class ItemBindFacilityAdapter : RecyclerView.Adapter<ItemBindFacilityAdapter.InnerHolder>() {

    private val mFacilityList = mutableListOf<Facility>()
    private lateinit var mListener: (facility: Facility, position: Int) -> Unit

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemBindFacilityAdapter.InnerHolder {
        val itemView = inflate(R.layout.item_bind_facility, parent)
        return InnerHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemBindFacilityAdapter.InnerHolder, position: Int) {
        val facility = mFacilityList[position]
        holder.run {
            facilityNameTv.text = facility.name
            facilityIdTv.text = facility.facilityId
            facilityBindTv.setOnClickListener {
                if (::mListener.isInitialized) mListener(facility, position)
            }
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
        val facilityNameTv: TextView = itemView.findViewById(R.id.facilityNameTv)
        val facilityIdTv: TextView = itemView.findViewById(R.id.facilityIdTv)
        val facilityBindTv: TextView = itemView.findViewById(R.id.facilityBindTv)
    }
}