package com.example.whanweather.ui.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.whanweather.R
import com.example.whanweather.logic.Repository
import com.example.whanweather.logic.dao.PlaceDao
import com.example.whanweather.ui.service.SettingService
import com.example.whanweather.ui.weather.WeatherActivity
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(SettingViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        weatherNotify.isChecked = Repository.getWeatherNotifyState()

        backButton.setOnClickListener {
            onBackPressed()
        }

        weatherNotify.setOnCheckedChangeListener { _, isChecked ->
            Repository.saveWeatherNotify(isChecked)
            if (isChecked) {
                val intent = Intent(this, SettingService::class.java)
                startService(intent)
            } else {
                val intent = Intent(this, SettingService::class.java)
                stopService(intent)
            }
            viewModel.weatherNotify = isChecked
        }

    }
}