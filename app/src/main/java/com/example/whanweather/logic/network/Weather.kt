package com.example.whanweather.logic.network

import com.example.whanweather.ui.search.DailyResponse
import com.example.whanweather.ui.search.LifeIndexResponse

data class Weather(val dailyResponse: DailyResponse, val lifeIndexResponse: LifeIndexResponse)