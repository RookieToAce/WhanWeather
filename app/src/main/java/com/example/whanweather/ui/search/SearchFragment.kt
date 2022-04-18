package com.example.whanweather.ui.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.whanweather.MainActivity
import com.example.whanweather.R
import com.example.whanweather.logic.model.getSky
import com.example.whanweather.ui.weather.WeatherActivity
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.fragment_place.*
import kotlinx.android.synthetic.main.place_item.*

/**
 * 实现Fragment
 */

class SearchFragment : Fragment() {

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

        searchPlaceEdit.addTextChangedListener {
            val content = it.toString()
            if (content.isNotEmpty()) {
                //当输入的不为空时，进入仓库层进行搜索，执行仓库层的逻辑，将content赋值给PlaceViewModel中的liveData对象
                viewModel.searchPlace(content)
            } else {
                placeCard.visibility = View.GONE
                viewModel.nowData.clear()
            }
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

            //
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
        }

    }

    private fun refreshPlaceCard() {
        val placeResponse = viewModel.nowData[0].location
        val nowResponse = viewModel.nowData[0].now
        Log.d("main", "size = ${viewModel.nowData.size}")
        placeName.text = placeResponse.name
        placeAddress.text = placeResponse.path
        nowWeatherImg.setImageResource(getSky(nowResponse.text).icon)
        val tempText = "${nowResponse.temperature} ℃"
        nowTemperatureText.text = tempText
    }

}







