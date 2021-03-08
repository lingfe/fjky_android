package com.fjkyly.paradise.provider

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.core.content.getSystemService
import com.blankj.utilcode.util.GsonUtils
import com.hjq.permissions.XXPermissions

object LocationProvider {

    private var count = 0
    private var mLocation: Location? = null

    @SuppressLint("MissingPermission")
    fun getLocationJson(
        context: Context
    ): String {
        XXPermissions.with(context)
            .permission(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            .request { _, all ->
                if (all) {
                    val locationManager = context.getSystemService<LocationManager>()
                    locationManager ?: return@request
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        AlarmManager.INTERVAL_FIFTEEN_MINUTES / 15,
                        1f,
                        LocationListener { location ->
                            locationUpdates(
                                location
                            )
                        })
                    val location =
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    locationUpdates(
                        location
                    )
                }
            }
        return GsonUtils.toJson(
            mapOf(
                "longitude" to mLocation?.longitude,
                "latitude" to mLocation?.latitude
            )
        )
    }

    /**
     * 获取位置对象
     */
    fun getLocation(context: Context): Location? =
        mLocation

    /**
     * 更新经纬度信息
     */
    private fun locationUpdates(location: Location?) {
        mLocation = location
        if (location == null) {
            Log.e(TAG, "locationUpdates: ===>经纬度获取失败，用户可能未授予定位权限！")
            return
        }
        // 经度
        val longitude = location.longitude
        // 纬度
        val latitude = location.latitude
        Log.d(
            TAG,
            "getLocationInfo: 第${++count}次获取===>经度：$longitude，纬度：$latitude"
        )
    }

    private const val TAG = "Location"

    // 默认的经度
    const val DEFAULT_LATITUDE = 39.908689

    // 默认的纬度
    const val DEFAULT_LONGITUDE = 116.397475
}