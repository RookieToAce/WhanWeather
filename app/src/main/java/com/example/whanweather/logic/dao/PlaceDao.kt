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

    private fun sharedPreferences() =
        WhanWeatherApplication.context.getSharedPreferences("whan_weather", Context.MODE_PRIVATE)


}