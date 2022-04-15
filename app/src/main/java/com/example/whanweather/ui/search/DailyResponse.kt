package com.example.whanweather.ui.search

import com.google.gson.annotations.SerializedName

data class DailyResponse(val results: List<Daily>) {

    data class Daily(val location: Location, val daily: List<Data>) {

        data class Data(
            val date: String,
            val low: String,
            val high: String,
            @SerializedName("text_day") val textDay: String,
            @SerializedName("text_night") val textNight: String,
            val humidity: String
        )

        data class Location(val name: String)

    }
}

