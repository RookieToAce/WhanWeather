package com.example.whanweather.ui.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import com.example.whanweather.logic.model.TimeUnits

class SettingService : Service() {

    companion object {
        const val TAG = "SettingService"
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate executed")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand executed")
        val alarmManager = getSystemService(ALARM_SERVICE)
        val time = TimeUnits.getNowTime()
        Log.d(TAG, time)

        val todayNotify = "07:00:00"
        val tomorrowNotify = "21:00:00"

        //发送广播 通知Activity创建一个通知来提醒
//        if (time == todayNotify) {
//            val intent = Intent("com.example.whanweather.TODAY_NOTIFY")
//            intent.setPackage(packageName)
//            sendBroadcast(intent)
//        } else if (time == tomorrowNotify) {
//            val intent = Intent("com.example.whanweather.TOMORROW_NOTIFY")
//            intent.setPackage(packageName)
//            sendBroadcast(intent)
//        }

            val intent1 = Intent("com.example.whanweather.TOMORROW_NOTIFY")
            intent1.setPackage(packageName)
            sendBroadcast(intent1)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy executed")
    }

}