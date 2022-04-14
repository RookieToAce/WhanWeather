package com.example.whanweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class WhanWeatherApplication : Application() {

    companion object {
        const val KEY = "SJcoPbBKLQ7A6D1fg"

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

}