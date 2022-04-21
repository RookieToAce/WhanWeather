package com.example.whanweather.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 封装所有网络API
 */

object WhanWeatherNetwork {

    /**
     * 创建搜寻地点的Retrofit网络请求Service
     * Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
     */
    private val placeService = ServiceCreator.create<NowService>()

    /**
     * 创建搜寻未来数据的Retrofit网络请求Service
     */
    private val dailyService = ServiceCreator.create<DailyService>()

    //在协程中进行网络请求，调用await()方法，将请求的结果返回
    //外部调用时，协程阻塞，直到服务器响应请求，await()方法会将解析出来的数据取出并返回至上一层

    /**
     * 请求地点数据
     */
    suspend fun searchPlace(location: String) = placeService.searchPlace(location).await()

    /**
     * 请求当天生活质量：穿衣、洗车、感冒、紫外线
     */
    suspend fun searchLifeIndex(location: String) = placeService.getLifeIndex(location).await()

    /**
     * 请求未来几天天气数据：目前只有今明后三天
     */
    suspend fun searchDaily(location: String) = dailyService.getDailyWeather(location).await()

}