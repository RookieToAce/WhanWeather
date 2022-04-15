package com.example.whanweather.logic.network

import com.example.whanweather.ui.search.DailyResponse
import com.example.whanweather.ui.search.LifeIndexResponse
import com.example.whanweather.ui.search.NowResponse

data class Weather(val daily: DailyResponse, val lifeIndex: LifeIndexResponse, val now: NowResponse)
