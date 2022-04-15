package com.example.whanweather.ui.search

import com.google.gson.annotations.SerializedName

data class LifeIndexResponse(val results: List<LifeIndex>) {

    data class LifeIndex(val location: Location, val suggestion: Suggestion) {

        data class Location(val name: String)

        data class Suggestion(
            @SerializedName("car_washing") val carWashing: LifeDescription,
            val dressing: LifeDescription,
            val flu: LifeDescription,
            val uv: LifeDescription
        )

        data class LifeDescription(val brief: String)

    }

}


//data class LifeIndexResponse(val location: Location, val suggestion: Suggestion) {
//
//    data class Location(val name: String)
//
//    data class Suggestion(
//        @SerializedName("car_washing") val carWashing: LifeDescription,
//        val dressing: LifeDescription,
//        val flu: LifeDescription,
//        val uv: LifeDescription
//    )
//
//    data class LifeDescription(val brief: String)
//
//}

