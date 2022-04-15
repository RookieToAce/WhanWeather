package com.example.whanweather.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.whanweather.logic.Repository

/**
 * 天气显示界面的ViewModel
 * 通过一个 searchLiveData 对象来实时监视，地点变化
 * 使用switchMap来观察这个 searchLiveData 对象并在switchMap()方法的转换函数中调用仓库层中
 * 定义的refreshWeather()方法。使得仓库层返回的LiveData对象就可以转换成一个可供Activity观察的LiveData。
 */

class WeatherViewModel : ViewModel() {

    private val searchLiveData = MutableLiveData<String>()

    val placeName = ""

    val weatherLiveData = Transformations.switchMap(searchLiveData) { location ->
        Repository.refreshWeather(location)
    }

    fun showWeather(location: String) {
        searchLiveData.value = location
    }

}