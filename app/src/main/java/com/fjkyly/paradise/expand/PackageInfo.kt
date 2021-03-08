package com.fjkyly.paradise.expand

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.amap.api.maps.model.LatLng
import com.fjkyly.paradise.base.App
import com.vondear.rxtool.RxAppTool

// 高德地图包名
const val GAO_DE_MAP = "com.autonavi.minimap"

// 百度地图包名
const val BAI_DU_MAP = "com.baidu.BaiduMap"

/**
 * 检查已安装的地图应用程序
 * 参数：需要检测的地图 APP 包名
 * 安装了地图 APP 则为 true ，否则为 false
 */
fun checkInstalledMapApp(mapAppList: List<String>): Boolean {
    // 获取所有已安装App信息
    val appInfoList = RxAppTool.getAllAppsInfo(App.appContext)
    Log.d(
        TAG,
        "initMapInfoWindow: ===>${if (appInfoList.isEmpty()) "无法获取到已安装的APP列表" else "已安装APP列表获取成功"}"
    )
    val installAppPackageList = arrayListOf<String>()
    for (appInfo in appInfoList) {
        installAppPackageList.add(appInfo.packageName)
    }
    // 返回一个包含此collection和指定collection包含的所有元素的集合。返回的集合保留原始集合的元素迭代顺序。
    val intersectList = installAppPackageList.intersect(mapAppList)
    for (packageName in intersectList) {
        Log.d(TAG, "initMapInfoWindow: ===>packageName：$packageName")
    }
    return intersectList.isNotEmpty()
}

/**
 * 启动用户手机上已安装的地图 APP
 */
fun launchMapApp(context: Context, latLng: LatLng) {
    //  打开手机上用户已经安装的导航APP进行导航
    val uri: Uri = Uri.parse("geo:${latLng.latitude},${latLng.longitude}")
    val openNavigationAPPIntent = Intent(Intent.ACTION_VIEW, uri)
    context.startActivity(openNavigationAPPIntent)
}

private const val TAG = "PackageInfo"