package com.example.whanweather.logic

import androidx.lifecycle.liveData
import com.example.whanweather.logic.network.WhanWeatherNetwork
import com.example.whanweather.ui.search.NowResponse
import com.example.whanweather.ui.search.Place
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

/**
 * 统一的仓库层入口
 * 主要工作：
 * 判断要请求的数据应该是从本地数据库获取还是从网络数据源中获取
 */

object Repository {

    fun searchPlace(location: String) = liveData(Dispatchers.IO) {
        val result = try {
            val placeResponse = WhanWeatherNetwork.searchPlace(location)
            if (placeResponse.results.isNotEmpty()) {
                val place = placeResponse.results
                Result.success(place)
            } else {
                Result.failure(RuntimeException("No response"))
            }
        } catch (e: Exception) {
            Result.failure<List<Place>>(e)
        }
        emit(result)
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