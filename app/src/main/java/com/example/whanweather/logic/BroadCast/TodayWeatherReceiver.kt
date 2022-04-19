package com.example.whanweather.logic.BroadCast

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.whanweather.WhanWeatherApplication
import com.example.whanweather.logic.Repository
import com.example.whanweather.logic.dao.PlaceDao
import com.example.whanweather.logic.model.getSky

class TodayWeatherReceiver : BroadcastReceiver() {

    companion object {
        const val TAG = "TodayWeatherReceiver"

        const val WEATHER_TOADY = 1
    }

    override fun onReceive(context: Context, intent: Intent) {

        if (PlaceDao.isPlaceSaved()) {
            val manager =
                WhanWeatherApplication.context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelToday = NotificationChannel(
                    "weatherToday",
                    "WeatherToday",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                val channelTomorrow = NotificationChannel(
                    "weatherTomorrow",
                    "WeatherTomorrow",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                manager.createNotificationChannel(channelToday)
                manager.createNotificationChannel(channelTomorrow)
            }
            //调用仓库东西应使用统一的仓库接口
            val place = Repository.getSavedPlace().location.name
            val sky = Repository.getSkyToday()
            val temperature = Repository.getTempToday()
            //val place = PlaceDao.getSavedPlace().location.name
            val text = "${place}市  $sky  气温: $temperature"
            val weatherToday =
                NotificationCompat.Builder(WhanWeatherApplication.context, "weatherToday")
                    .setContentTitle("今天天气情况").setContentText(text).setSmallIcon(getSky(sky).icon)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            WhanWeatherApplication.context.resources,
                            getSky(sky).icon
                        )
                    )
                    .build()
            manager.notify(WEATHER_TOADY, weatherToday)
        } else {
            Log.d(TAG, "No place saved!")
        }


        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
    }
}