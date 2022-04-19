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

class TomorrowReceiver : BroadcastReceiver() {

    companion object {
        const val TAG = "TomorrowReceiver"

        const val WEATHER_TOMORROW = 2
    }

    override fun onReceive(context: Context, intent: Intent) {

        if (PlaceDao.isPlaceSaved()) {
            val manager =
                WhanWeatherApplication.context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelTomorrow = NotificationChannel(
                    "weatherTomorrow",
                    "WeatherTomorrow",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                manager.createNotificationChannel(channelTomorrow)
            }
            //调用仓库东西应使用统一的仓库接口
            //val place = PlaceDao.getSavedPlace().location.name
            val place = Repository.getSavedPlace().location.name
            val sky = Repository.getSkyTomorrow()
            val temperature = Repository.getTempTomorrow()
            val text = "${place}市  $sky  气温: $temperature"
            val weatherTomorrow =
                NotificationCompat.Builder(WhanWeatherApplication.context, "weatherTomorrow")
                    .setContentTitle("明天天气情况").setContentText(text).setSmallIcon(getSky(sky).icon)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            WhanWeatherApplication.context.resources,
                            getSky(sky).icon
                        )
                    )
                    .build()
            manager.notify(WEATHER_TOMORROW, weatherTomorrow)
        } else {
            Log.d(TAG, "No place saved!")
        }
    }
}