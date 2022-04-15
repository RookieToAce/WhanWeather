package com.example.whanweather.logic

import androidx.lifecycle.liveData
import com.example.whanweather.logic.network.Weather
import com.example.whanweather.logic.network.WhanWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

/**
 * 仓库层包含：网络数据来源和本地数据
 * 定义的search* 方法，用于确定是从网络获取数据还是从本地仓库中获取
 * 统一的仓库层入口
 * 主要工作：
 * 判断要请求的数据应该是从本地数据库获取还是从网络数据源中获取
 */

object Repository {

    /**
     * 因为weather对象是由 daily 和 lifeIndex 两个对象组成
     * 这两个对象分别来自于不同的网络请求，并发可以提升执行效率，但是想着同时得到他们的响应结果，才来继续执行
     * 需要使用async函数，在协程中跑完，返回一个延迟(deferred)结果，需要数据时调用await()即可获得数据
     */


    fun searchPlace(location: String) = fire(Dispatchers.IO) {
        val placeResponse = WhanWeatherNetwork.searchPlace(location)
        if (placeResponse.results.isNotEmpty()) {
            val place = placeResponse.results
            Result.success(place)
        } else {
            Result.failure(RuntimeException("No response"))
        }
    }

    fun refreshWeather(location: String) = fire(Dispatchers.IO) {

        coroutineScope {
            val deferredDaily = async {
                WhanWeatherNetwork.searchDaily(location)
            }
            val deferredLifeIndex = async {
                WhanWeatherNetwork.searchLifeIndex(location)
            }

            val dailyResponse = deferredDaily.await()
            val lifeIndexResponse = deferredLifeIndex.await()

            if (dailyResponse.results.location.name.isNotEmpty() && lifeIndexResponse.result.location.name.isNotEmpty()) {
                val weather = Weather(dailyResponse, lifeIndexResponse)
                Result.success(weather)
            } else {
                Result.failure(RuntimeException("No Response"))
            }
        }

//        val dailyResponse = WhanWeatherNetwork.searchDaily(location)
//        if (dailyResponse.results.location.name.isNotEmpty()) {
//            val daily = dailyResponse.results
//            Result.success(daily)
//        } else {
//            Result.failure(java.lang.RuntimeException("No response"))
//        }
    }


    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }

}