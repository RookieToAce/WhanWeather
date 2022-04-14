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

    private val placeService = ServiceCreator.create<NowService>()

    //在协程中进行网络请求，调用await()方法，将请求的结果返回
    //外部调用时，协程阻塞，直到服务器响应请求，await()方法会将解析出来的数据取出并返回至上一层
    suspend fun searchPlace(location: String) = placeService.searchPlace(location).await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

}