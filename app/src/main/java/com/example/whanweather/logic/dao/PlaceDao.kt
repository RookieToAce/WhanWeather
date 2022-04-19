package com.example.whanweather.logic.dao

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.whanweather.WhanWeatherApplication
import com.example.whanweather.ui.search.NowResponse
import com.google.gson.Gson

object PlaceDao {

    //将Place对象存储到 SharedPreferences文件中。并使用GSON将Place对象转化为一个JSON字符串，方便存储
    fun savePlace(place: NowResponse.Place) {
        sharedPreferences().edit {
            putString("place", Gson().toJson(place))
        }
    }

    //读取保存的对象，将SharedPreferences文件中保存的JSON格式的Place对象使用GSON解析成Place对象并返回！
    fun getSavedPlace(): NowResponse.Place {
        val placeJson = sharedPreferences().getString("place", "")
        return Gson().fromJson(placeJson, NowResponse.Place::class.java)
    }

    //判断是否存在
    fun isPlaceSaved() = sharedPreferences().contains("place")

    //存储天气监听状态
    fun saveWeatherNotify(state: Boolean) {
        sharedPreferences().edit {
            putBoolean("weather_notify", state)
        }
    }

    //
    fun getWeatherNotifyState(): Boolean {
        return sharedPreferences().getBoolean("weather_notify", false)
    }

    /**
     * 存储今天天气情况，温度等信息
     */

    fun saveWeatherToday(sky: String, temperature: String) {
        sharedPreferences().edit {
            putString("skyToday", sky)
            putString("tempToday", temperature)
        }
    }

    fun getSkyToday(): String {
        return sharedPreferences().getString("skyToday","").toString()
    }

    fun getTempToday(): String {
        return sharedPreferences().getString("tempToday","").toString()
    }


    /**
     * 存储明天天气情况，温度等信息
     */

    fun saveWeatherTomorrow(sky: String, temperature: String) {
        sharedPreferences().edit {
            putString("skyTomorrow", sky)
            putString("tempTomorrow", temperature)
        }
    }

    fun getSkyTomorrow(): String {
        return sharedPreferences().getString("skyTomorrow","").toString()
    }

    fun getTempTomorrow(): String {
        return sharedPreferences().getString("tempTomorrow","").toString()
    }

    private fun sharedPreferences() =
        WhanWeatherApplication.context.getSharedPreferences("whan_weather", Context.MODE_PRIVATE)


}