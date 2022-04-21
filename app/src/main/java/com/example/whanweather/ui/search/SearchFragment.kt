package com.example.whanweather.ui.search

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.whanweather.MainActivity
import com.example.whanweather.R
import com.example.whanweather.WhanWeatherApplication
import com.example.whanweather.logic.Repository
import com.example.whanweather.logic.dao.HistoryDao
import com.example.whanweather.logic.entity.PlaceRecord
import com.example.whanweather.logic.model.TimeUnits
import com.example.whanweather.logic.model.getSky
import com.example.whanweather.logic.weatherdatabase.WeatherDatabase
import com.example.whanweather.ui.weather.WeatherActivity
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.fragment_place.*
import kotlinx.android.synthetic.main.place_item.*
import kotlin.concurrent.thread

/**
 * 实现Fragment
 */

class SearchFragment : Fragment() {

    companion object {
        const val TAG = "SearchFragment"
    }

    private val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /**
         * 使用Repository进行了封装，使用统一的仓库层入口
         */
//        historyDao = WeatherDatabase.getDatabase(WhanWeatherApplication.context).HistoryDao()

        //先进性判断，如果有地点保存，则直接进入保存的地点的天气情况，避免主界面是搜索界面！！！
        //加了一层逻辑判断
        //因为将placeFragment嵌入到了WeatherActivity中，会出现无限跳转情况
        if (activity is MainActivity && viewModel.isPlaceSaved()) {
            val place = viewModel.getSavedPlace()
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("place_name", place.location.name)
            }
            startActivity(intent)
            activity?.finish()
            return
        }

        searchPlaceEdit.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null) && KeyEvent.KEYCODE_ENTER == event.keyCode && KeyEvent.ACTION_DOWN == event.action
            ) {
                val content = searchPlaceEdit.text.toString()
                if (content.isNotEmpty()) {
                    //当输入的不为空时，进入仓库层进行搜索，执行仓库层的逻辑，将content赋值给PlaceViewModel中的liveData对象
                    viewModel.searchPlace(content)
                } else {
                    placeCard.visibility = View.GONE
                    viewModel.nowData.clear()
                }
            }
            false
        }

        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer {
            val placeData = it.getOrNull()
            if (placeData != null) {
                placeCard.visibility = View.VISIBLE
                viewModel.nowData.clear()
                viewModel.nowData.addAll(placeData)
                refreshPlaceCard()
            } else {
                Toast.makeText(activity, "未查询到任何地点", Toast.LENGTH_SHORT).show()
                it.exceptionOrNull()?.printStackTrace()
            }
        })

        placeCard.setOnClickListener {
            val place = viewModel.nowData[0]
            val fragActivity = activity
            if (fragActivity is WeatherActivity) {
                fragActivity.drawerLayout.closeDrawers()
                fragActivity.viewModel.placeName = place.location.name
                fragActivity.refreshWeatherInfo()
            } else {
                val intent = Intent(context, WeatherActivity::class.java).apply {
                    putExtra("place_name", placeName.text)
                }
                startActivity(intent)
                fragActivity?.finish()
            }
            viewModel.savePlace(place)
            saveWeather()
        }

    }

    private fun saveWeather() {

        if (viewModel.nowData.isNotEmpty()) {
            val place = viewModel.nowData[0].location.name
            val temperature = viewModel.nowData[0].now.temperature
            val sky = viewModel.nowData[0].now.text

            thread {
                //val weather = PlaceRecord(place, temperature, sky)

                Log.d(TAG, "contain: ${Repository.containPlace(place)}")

                if (Repository.containPlace(place)) {

                    val weather = Repository.getPlaceFormName(place)
                    Log.d(TAG, "$weather")
                    weather.temperature = temperature
                    weather.sky = sky

                    Repository.updatePlace(weather)
                    Log.d(TAG, "${place}'s weather info is changed!")
                } else {

                    val weather = PlaceRecord(place, temperature, sky)

                    weather.id = Repository.insertPlace(weather)
                    Log.d(TAG, "${place}'s id: ${weather.id}")
                }
            }
        }

    }

    private fun refreshPlaceCard() {
        val placeResponse = viewModel.nowData[0].location
        val nowResponse = viewModel.nowData[0].now
        placeName.text = placeResponse.name
        //placeAddress.text = placeResponse.path

        val timeNow = TimeUnits.getNowHour()
        val skyNow = nowResponse.text
        if (timeNow.toInt() in 7..18) {
            nowWeatherImg.setImageResource(getSky(skyNow).icon)
        } else {
            if (skyNow == "晴" || skyNow == "多云") {
                val sky = "${skyNow}_夜"
                nowWeatherImg.setImageResource(getSky(sky).icon)
            } else {
                nowWeatherImg.setImageResource(getSky(skyNow).icon)
            }
        }

        val tempText = "${nowResponse.temperature}°"
        nowTemperatureText.text = tempText
    }

}







