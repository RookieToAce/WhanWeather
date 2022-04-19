package com.example.whanweather.ui.placeManager

import androidx.lifecycle.ViewModel
import com.example.whanweather.ui.search.NowResponse

class PlaceManagerViewModel : ViewModel() {

    val placeSaved = ArrayList<NowResponse.Place>()

}