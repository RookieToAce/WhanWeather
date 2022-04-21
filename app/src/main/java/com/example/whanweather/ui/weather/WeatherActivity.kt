package com.example.whanweather.ui.weather

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.whanweather.R
import com.example.whanweather.logic.Repository
import com.example.whanweather.logic.entity.PlaceRecord
import com.example.whanweather.logic.model.TimeUnits
import com.example.whanweather.logic.model.getSky
import com.example.whanweather.logic.network.Weather
import com.example.whanweather.ui.search.SearchActivity
import com.example.whanweather.ui.search.SearchFragment
import com.example.whanweather.ui.setting.SettingActivity
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.forecast.*
import kotlinx.android.synthetic.main.life_index.*
import kotlinx.android.synthetic.main.now.*
import kotlin.concurrent.thread

class WeatherActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "WeatherActivity"
    }

    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val controller = ViewCompat.getWindowInsetsController(window.decorView)
        setContentView(R.layout.activity_weather)

        //通过搜索界面搜索出来的点击事件的Intent来触发这个天气界面

        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
            Log.d(TAG, "${viewModel.placeName}")
            viewModel.showWeather(viewModel.placeName)
        }
//        refreshWeatherInfo()

        viewModel.weatherLiveData.observe(this, Observer {
            val weather = it.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "未查询到任何地点", Toast.LENGTH_SHORT).show()
                it.exceptionOrNull()?.printStackTrace()
            }
            swipeRefresh.isRefreshing = false
        })

        //刷新状态
        swipeRefresh.setColorSchemeResources(com.google.android.material.R.color.design_default_color_primary)
        refreshWeatherInfo()
        //刷新的下拉监听器
        swipeRefresh.setOnRefreshListener {
            refreshWeatherInfo()
        }

        //搜索界面
        //openDrawer打开滑动菜单
        searchBtn.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish()
//            drawerLayout.openDrawer(GravityCompat.START)
        }

        //监听DrawerLayout状态，滑动菜单隐藏，输入法隐藏！
        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {}

            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(
                    drawerView.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }

            override fun onDrawerStateChanged(newState: Int) {}

        })

        //Setting界面
        settingButton.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

    }

    fun refreshWeatherInfo() {
        viewModel.showWeather(viewModel.placeName)
        refreshDatabase()
        swipeRefresh.isRefreshing = true
    }

    private fun refreshDatabase() {
        val place = weatherPlaceName.text.toString()
        val temp = currentTemp.text.toString()
        val sky = currentSky.text.toString()

        if (place != "" && temp != "" && sky != "") {
            thread {

                Log.d(TAG, "$place contain: ${Repository.containPlace(place)}")

                if (Repository.containPlace(place)) {

                    val weather = Repository.getPlaceFormName(place)
                    weather.temperature = temp
                    weather.sky = sky

                    Repository.updatePlace(weather)
                    Log.d(TAG, "${place}'s weather info is changed!")
                } else {

                    val weather = PlaceRecord(place, temp, sky)

                    weather.id = Repository.insertPlace(weather)
                    Log.d(TAG, "${place}'s id: ${weather.id}")
                }
            }
        }

    }

    private fun showWeatherInfo(weather: Weather) {

        //实时数据
        val nowData = weather.now.results[0]
        //未来数据
        val dailyData = weather.daily.results[0]
        //生活指数
        val lifeIndex = weather.lifeIndex.results[0]

        //设置天气首页背景
        wholeLayout.setBackgroundResource(getSky(nowData.now.text).bg)
//        weatherLayout.setBackgroundResource(getSky(nowData.now.text).bg)

        //填充now.xml布局中的数据
        weatherPlaceName.text = nowData.location.name
        val tempText = nowData.now.temperature
        currentTemp.text = tempText
        currentSky.text = nowData.now.text
        val humidityText = "湿度:${dailyData.daily[0].humidity}"
        //currentHumidity.text = humidityText

        //填充forecast.xml中的数据
        forecastLayout.removeAllViews()    //因为是动态填充，以防再次填充时有旧数据
        val days = dailyData.daily.size
        for (i in 0 until 3) {
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

            dataInfo.text = when (i) {
                0 -> "今天"
                1 -> "明天"
                2 -> "后天"
                else -> ""
            }

            //dataInfo.text = dataInfoText
            val timeNow = TimeUnits.getNowHour()

            if (i == 0) {
                Log.d(TAG, "Time: ${timeNow}点")
                if (timeNow.toInt() in 7..18) {
                    skyIcon.setImageResource(getSky(skyDay).icon)
                } else {
                    if (skyDay == "晴" || skyDay == "多云") {
                        val sky = "${skyDay}_夜"
                        skyIcon.setImageResource(getSky(sky).icon)
                    } else {
                        skyIcon.setImageResource(getSky(skyDay).icon)
                    }
                }
            } else {
                skyIcon.setImageResource(getSky(skyDay).icon)
            }


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

            //向仓库保存数据
            if (i == 0) {
                Repository.saveWeatherToday(skyInfo.text.toString(), tempInfo.text.toString())
            }

            if (i == 1) {
                Repository.saveWeatherTomorrow(skyInfo.text.toString(), tempInfo.text.toString())
            }

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