package com.fjkyly.paradise.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.content.getSystemService

/**
 * 提醒服务
 */
class RemindService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        registerAlarmClock()
        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * 注册闹钟提醒
     */
    private fun registerAlarmClock() {
        val alarmManager = getSystemService<AlarmManager>()
        alarmManager ?: return
        val triggerTime =
            System.currentTimeMillis() + 10000
        val pendingIntent =
            PendingIntent.getBroadcast(this, 0, Intent(this, AlarmReceiver::class.java), 0)
        val alarmClockInfo = AlarmManager.AlarmClockInfo(triggerTime, pendingIntent)
        alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
        Log.d(TAG, "registerAlarmClock: ===>注册成功！")
    }

    override fun onDestroy() {
        super.onDestroy()
        unRegisterAlarmClock()
    }

    /**
     * 注销闹钟提醒
     */
    private fun unRegisterAlarmClock() {
        // 在 Service 结束后关闭 AlarmManager
        val alarmManager = getSystemService<AlarmManager>()
        alarmManager ?: return
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        alarmManager.cancel(pendingIntent)
        Log.d(TAG, "registerAlarmClock: ===>注销成功！")
    }

    companion object {
        private const val TAG = "RemindService"
    }
}