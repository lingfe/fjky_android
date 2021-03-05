package com.fjkyly.paradise.network

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.core.content.getSystemService
import com.hjq.permissions.XXPermissions

object LocationProvider {

    private var count = 0

    @SuppressLint("MissingPermission")
    fun getLocationInfo(context: Context) {
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
                        1000L, 1f, LocationListener { location ->
                            locationUpdates(location)
                        })
                    val location =
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    locationUpdates(location)
                }
            }
    }

    private fun locationUpdates(location: Location?) {
        location ?: return
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
}