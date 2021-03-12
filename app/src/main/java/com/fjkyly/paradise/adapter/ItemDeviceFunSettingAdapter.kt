package com.fjkyly.paradise.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fjkyly.paradise.R
import com.fjkyly.paradise.expand.inflate
import com.fjkyly.paradise.model.DeviceSettingFun

class ItemDeviceFunSettingAdapter :
    RecyclerView.Adapter<ItemDeviceFunSettingAdapter.InnerHolder>() {

    private val mDeviceFunList = arrayListOf<DeviceSettingFun.Data.DevFun>()
    private lateinit var mListener: (deviceFun: DeviceSettingFun.Data.DevFun, position: Int) -> Unit

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemDeviceFunSettingAdapter.InnerHolder {
        val itemView = inflate(R.layout.item_device_fun_setting, parent)
        return InnerHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemDeviceFunSettingAdapter.InnerHolder, position: Int) {
        val deviceFun = mDeviceFunList[position]
        holder.run {
            itemView.setOnClickListener {
                if (::mListener.isInitialized) mListener(deviceFun, position)
            }
            menuNameTv.text = deviceFun.funName
        }
    }

    fun setOnItemClickListener(listener: (deviceFun: DeviceSettingFun.Data.DevFun, position: Int) -> Unit) {
        mListener = listener
    }

    override fun getItemCount(): Int = mDeviceFunList.size

    fun setData(data: List<DeviceSettingFun.Data.DevFun>) {
        mDeviceFunList.run {
            clear()
            addAll(data)
            notifyDataSetChanged()
        }
    }

    inner class InnerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val menuNameTv: TextView = itemView.findViewById(R.id.menuNameTv)
    }
}