package com.example.whanweather.ui.search

import com.google.gson.annotations.SerializedName

data class DailyResponse(val results: Result) {

    data class Result(val location: Location, val daily: List<Daily>) {

        data class Daily(
            val lowTemp: String,
            val highTemp: String,
            @SerializedName("text_day") val textDay: String,
            @SerializedName("text_night") val textNight: String,
            val humidity: String
        )

        data class Location(val name: String)

    }
}

//data class DailyResponse(val location: Location, val daily: List<Daily>) {
//
//    data class Daily(
//        val lowTemp: String,
//        val highTemp: String,
//        @SerializedName("text_day") val textDay: String,
//        @SerializedName("text_night") val textNight: String,
//        val humidity: String
//    )
//
//    data class Location(val name: String)
//
//}
