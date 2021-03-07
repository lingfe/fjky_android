package com.fjkyly.paradise.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.amap.api.maps.AMap
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.fjkyly.paradise.R
import com.fjkyly.paradise.base.App

/**
 * 自定义地图信息窗口
 */
class CustomInfoWindowAdapter : AMap.InfoWindowAdapter {

    // 在地图上标记位置的经纬度值
    private lateinit var latLng: LatLng
    // 点标记的标题
    private var title = ""
    // 点标记的内容
    private var snippet = ""

    override fun getInfoContents(marker: Marker?): View? {
        marker ?: return null
        initData(marker)
        return initView()
    }

    private fun initData(marker: Marker) {
        marker.let {
            latLng = it.position
            title = it.title
            snippet = it.snippet
        }
    }

    @SuppressLint("InflateParams")
    private fun initView(): View {
        val view =
            LayoutInflater.from(App.appContext).inflate(R.layout.custom_map_info_window, null)
        view.run {
            val mapInfoWindowContentTv: TextView = findViewById(R.id.mapInfoWindowContentTv)
            mapInfoWindowContentTv.text = snippet
        }
        return view
    }

    override fun getInfoWindow(marker: Marker?): View? {
        return null
    }
}