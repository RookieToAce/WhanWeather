package com.example.whanweather.ui.search

data class NowResponse(val results: List<Place>)

data class Place(val location: Location, val now: Now) {

    data class Location(val name: String, val path: String)

    data class Now(val text: String, val temperature: String)

}


