package com.example.whanweather.logic.network

import com.example.whanweather.WhanWeatherApplication
import com.example.whanweather.ui.search.DailyResponse
import com.example.whanweather.ui.search.LifeIndexResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DailyService {

//    获取未来几天的数据
//    https://api.seniverse.com/v3/weather/daily.json?key=SJcoPbBKLQ7A6D1fg&location=beijing&language=zh-Hans&unit=c&start=0&days=5

    @GET("/v3/weather/daily.json?key=${WhanWeatherApplication.KEY}&language=zh-Hans&start=0")
    fun getDailyWeather(@Query("location") location: String): Call<DailyResponse>

}