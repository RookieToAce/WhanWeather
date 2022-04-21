package com.example.whanweather.ui.placeManager

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.whanweather.R
import com.example.whanweather.logic.entity.PlaceRecord
import com.example.whanweather.logic.model.getSky
import com.example.whanweather.ui.weather.WeatherActivity

class HistoryAdapter(
    private val activity: AppCompatActivity,
    private val placeList: List<PlaceRecord>
) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    companion object {
        const val TAG = "HistoryAdapter"
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.placeName)
        val nowWeatherImg: ImageView = view.findViewById(R.id.nowWeatherImg)
        val nowTemperatureText: TextView = view.findViewById(R.id.nowTemperatureText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        val tempText = "${place.temperature}°"
        holder.nowTemperatureText.text = tempText
        holder.nowWeatherImg.setImageResource(getSky(place.sky).icon)

        holder.itemView.setOnClickListener {

            val intent1 = Intent(activity.baseContext,WeatherActivity::class.java).apply {
                putExtra("place_name", place.name)
            }
            activity.startActivity(intent1)
            Log.d(TAG,"You clicked ${place.name}")
        }

    }

    override fun getItemCount(): Int = placeList.size
}