package com.fjkyly.paradise.adapter

import android.view.View
import android.view.ViewConfiguration
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

    private var lastTime = 0L

    override fun onBindViewHolder(holder: ItemDeviceFunSettingAdapter.InnerHolder, position: Int) {
        val deviceFun = mDeviceFunList[position]
        holder.run {
            itemView.setOnClickListener {
                if (::mListener.isInitialized) {
                    val currentTime = System.currentTimeMillis()
                    val singleClick =
                        currentTime - lastTime > ViewConfiguration.getDoubleTapTimeout()
                    // 如果只有用户的手指是单击时才回调，防止用户连续点击
                    if (singleClick) mListener(deviceFun, position)
                    lastTime = currentTime
                }
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
        val menuNameTv: TextView = itemView.findViewById(R.id.funNameTv)
    }
}