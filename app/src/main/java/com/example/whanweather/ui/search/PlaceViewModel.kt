package com.example.whanweather.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.whanweather.logic.Repository

class PlaceViewModel : ViewModel() {

    /**
     * 将搜索框的EditView中的输入值作为一个liveData，使用Transformations.switchMap对该值进行监听，
     * 当发生变化时 ，调用Repository的方法，进行网络请求，同时将返回的LiveData对象转换成一个可供
     * Activity观察的LiveData对象。
     */

    private val searchLiveData = MutableLiveData<String>()

    val nowData = ArrayList<NowResponse.Place>()

    val placeLiveData = Transformations.switchMap(searchLiveData) { location ->
        Repository.searchPlace(location)
    }

    fun searchPlace(location: String) {
        searchLiveData.value = location
    }

    //对PlaceViewModel中的数据进行处理，放在ViewModel中，不建议放在Activity中进行处理
    //规范点应该写入线程，并使用liveData对象来进行观察。
    fun savePlace(place: NowResponse.Place) = Repository.savePlace(place)

    fun getSavedPlace() = Repository.getSavedPlace()

    fun isPlaceSaved() = Repository.isPlaceSaved()

}