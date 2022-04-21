package com.example.whanweather.logic

import androidx.lifecycle.liveData
import com.example.whanweather.WhanWeatherApplication
import com.example.whanweather.logic.dao.PlaceDao
import com.example.whanweather.logic.entity.PlaceRecord
import com.example.whanweather.logic.network.Weather
import com.example.whanweather.logic.network.WhanWeatherNetwork
import com.example.whanweather.logic.weatherdatabase.WeatherDatabase
import com.example.whanweather.ui.search.NowResponse
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

    private val historyDao = WeatherDatabase.getDatabase(WhanWeatherApplication.context).HistoryDao()

    //调用网络请求！！
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
            val deferredNow = async {
                WhanWeatherNetwork.searchPlace(location)
            }

            val dailyResponse = deferredDaily.await()
            val lifeIndexResponse = deferredLifeIndex.await()
            val nowResponse = deferredNow.await()

            if (dailyResponse.results.isNotEmpty()
                && lifeIndexResponse.results.isNotEmpty()
                && nowResponse.results.isNotEmpty()
            ) {
                val weather = Weather(dailyResponse, lifeIndexResponse, nowResponse)
                Result.success(weather)
            } else {
                Result.failure(RuntimeException("No response"))
            }

        }

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


    //从本地存储SharedPreferences文件中增加、查询、判断是否存在等！！！
    fun savePlace(place: NowResponse.Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()

    fun saveWeatherToday(sky: String, temperature: String) =
        PlaceDao.saveWeatherToday(sky, temperature)

    fun saveWeatherTomorrow(sky: String, temperature: String) =
        PlaceDao.saveWeatherTomorrow(sky, temperature)

    fun getSkyToday() = PlaceDao.getSkyToday()
    fun getSkyTomorrow() = PlaceDao.getSkyTomorrow()

    fun getTempToday() = PlaceDao.getTempToday()
    fun getTempTomorrow() = PlaceDao.getTempTomorrow()


    /**
     * 天气提醒
     */
    fun getWeatherNotifyState() = PlaceDao.getWeatherNotifyState()

    fun saveWeatherNotify(state: Boolean) = PlaceDao.saveWeatherNotify(state)

    /**
     * 数据库增删改查操作
     */

    fun getPlacesFromDatabase() = historyDao.loadAllPlaces()
    fun insertPlace(placeRecord: PlaceRecord) = historyDao.insertPlace(placeRecord)
    fun delete(place: PlaceRecord) = historyDao.delete(place)
    fun containPlace(name: String) = historyDao.containPlace(name)
    fun updatePlace(newPlace: PlaceRecord) = historyDao.updatePlace(newPlace)
    fun deleteAll() = historyDao.deleteAll()
    fun getPlaceFormName(name: String) = historyDao.getPlaceFormName(name)
}