package com.fjkyly.paradise.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * 闹钟提醒接收者
 */
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive: ===>闹钟提醒服务执行了")
        context ?: return
        // 再次启动 RemindService 这个服务，从而可以循环提醒
        val startRemindIntent = Intent(context, RemindService::class.java)
        context.startService(startRemindIntent)
    }

    companion object {
        private const val TAG = "AlarmReceiver"
    }
}