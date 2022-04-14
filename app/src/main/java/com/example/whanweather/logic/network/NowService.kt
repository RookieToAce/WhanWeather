package com.example.whanweather.logic.network

import com.example.whanweather.WhanWeatherApplication
import com.example.whanweather.ui.search.NowResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NowService {

    //https://api.seniverse.com/v3/weather/now.json?key=SJcoPbBKLQ7A6D1fg&location=beijing&language=zh-Hans&unit=c

    @GET("v3/weather/now.json?key=${WhanWeatherApplication.KEY}&language=zh-Hans&unit=c")
    fun searchPlace(@Query("location") location: String): Call<NowResponse>

}