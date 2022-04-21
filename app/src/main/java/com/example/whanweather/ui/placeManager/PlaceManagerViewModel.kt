package com.example.whanweather.ui.placeManager

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.whanweather.logic.Repository
import com.example.whanweather.logic.entity.PlaceRecord
import com.example.whanweather.ui.search.NowResponse
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class PlaceManagerViewModel : ViewModel() {

    val placeSaved = ArrayList<PlaceRecord>()

//    suspend fun initPlaceList():  List<PlaceRecord> {
//
//        coroutineScope {
//            val savedPlaces = Repository.getPlacesFromDatabase()
//            placeSaved.addAll(savedPlaces)
//            val size = placeSaved.size
//            Log.d("nnn","$size")
//        }
//        return placeSaved
//    }

}