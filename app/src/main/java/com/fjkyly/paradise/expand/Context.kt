package com.fjkyly.paradise.expand

import android.app.*
import android.app.usage.NetworkStatsManager
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.telephony.TelephonyManager
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager

inline fun <reified T> Context.startActivity() {
    startActivity(Intent(this, T::class.java))
}

inline fun <reified T> Context.getService(): T = getSystemService(T::class.java)

/**
 * 剪贴板管理
 */
val Context.clipboardManager: ClipboardManager
    get() = getService()

/**
 * 窗口管理
 */
val Context.windowManager: WindowManager
    get() = getService()

/**
 * Activity 管理
 */
val Context.activityManager: ActivityManager
    get() = getService()

/**
 * 闹钟管理
 */
val Context.alarmManager: AlarmManager
    get() = getService()

/**
 * 键盘管理
 */
val Context.keyguardManager: KeyguardManager
    get() = getService()

/**
 * 搜索管理
 */
val Context.searchManager: SearchManager
    get() = getService()

/**
 * 连接管理
 */
val Context.connectivityManager: ConnectivityManager
    get() = getService()

/**
 * WiFi 管理
 */
val Context.wifiManager: WifiManager
    get() = getService()

/**
 * 音频管理
 */
val Context.audioManager: AudioManager
    get() = getService()

/**
 * 电话管理
 */
val Context.telephonyManager: TelephonyManager
    get() = getService()

/**
 * 输入法管理
 */
val Context.inputMethodManager: InputMethodManager
    get() = getService()

/**
 * UI模式管理
 */
val Context.uiModeManager: UiModeManager
    get() = getService()

/**
 * 电池管理
 */
val Context.batteryManager: BatteryManager
    get() = getService()

/**
 * 网络状态管理
 */
val Context.networkStatsManager: NetworkStatsManager
    get() = getService()

/**
 * 包管理
 */
val Context.packageManager: PackageManager
    get() = getService()