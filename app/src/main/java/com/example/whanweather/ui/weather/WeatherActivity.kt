package com.example.whanweather.ui.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.whanweather.R
import com.example.whanweather.logic.model.getSky
import com.example.whanweather.logic.network.Weather
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.forecast.*
import kotlinx.android.synthetic.main.life_index.*
import kotlinx.android.synthetic.main.now.*

class WeatherActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)


        //通过搜索界面搜索出来的点击事件的Intent来触发这个天气界面
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }

        viewModel.weatherLiveData.observe(this, Observer {

            val weather = it.getOrNull()
            if (weather != null) {

                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "未查询到任何地点", Toast.LENGTH_SHORT).show()
                it.exceptionOrNull()?.printStackTrace()
            }
        })

        val text = viewModel.placeName
        viewModel.showWeather(viewModel.placeName)

    }

    private fun showWeatherInfo(weather: Weather) {

        //实时数据
        val nowData = weather.now.results[0]
        //未来数据
        val dailyData = weather.daily.results[0]
        //生活指数
        val lifeIndex = weather.lifeIndex.results[0]

        //设置天气首页背景
        weatherLayout.setBackgroundResource(getSky(nowData.now.text).bg)

        //填充now.xml布局中的数据
        weatherPlaceName.text = nowData.location.name
        val tempText = "${nowData.now.temperature} ℃"
        currentTemp.text = tempText
        currentSky.text = nowData.now.text
        val humidityText = "湿度:${dailyData.daily[0].humidity}"
        currentHumidity.text = humidityText

        //填充forecast.xml中的数据
        forecastLayout.removeAllViews()    //因为是动态填充，以防再次填充时有旧数据
        val days = dailyData.daily.size
        for (i in 0 until days) {
            val dataInfoText = dailyData.daily[i].date
            val skyDay = dailyData.daily[i].textDay
            val skyNight = dailyData.daily[i].textNight
            val tempHigh = dailyData.daily[i].high
            val tempLow = dailyData.daily[i].low

            //需要获取View，从而动态创建View，实现动态加载
            //forecastLayout为希望向哪个父布局中进行填充
            val view =
                LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false)

            val dataInfo = view.findViewById<TextView>(R.id.dataInfo)
            val skyIcon = view.findViewById<ImageView>(R.id.skyIcon)
            val skyInfo = view.findViewById<TextView>(R.id.skyInfo)
            val tempInfo = view.findViewById<TextView>(R.id.tempInfo)

            dataInfo.text = dataInfoText
            skyIcon.setImageResource(getSky(skyDay).icon)
            if (skyDay == skyNight) {
                skyInfo.text = skyDay
            } else {
                val tmpText = "${skyDay}转${skyNight}"
                skyInfo.text = tmpText
            }

            val temperatureText = "${tempHigh}°/${tempLow}°"
            tempInfo.text = temperatureText

            //添加进组件
            forecastLayout.addView(view)
        }

        //生活指数板块
        lifeIndexLayout.background.alpha = 180
        coldRiskText.text = lifeIndex.suggestion.flu.brief
        dressingText.text = lifeIndex.suggestion.dressing.brief
        ultravioletText.text = lifeIndex.suggestion.uv.brief
        carWashingText.text = lifeIndex.suggestion.carWashing.brief
        
        weatherLayout.visibility = View.VISIBLE

    }

}