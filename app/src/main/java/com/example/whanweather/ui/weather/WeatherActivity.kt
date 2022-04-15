package com.example.whanweather.ui.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

class WeatherActivity : AppCompatActivity() {

    val viewModel by lazy { ViewModelProvider(this,) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}