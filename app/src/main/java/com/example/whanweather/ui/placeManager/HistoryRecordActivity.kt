package com.example.whanweather.ui.placeManager

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whanweather.R
import com.example.whanweather.logic.Repository
import com.example.whanweather.logic.entity.PlaceRecord
import com.example.whanweather.ui.search.NowResponse
import kotlinx.android.synthetic.main.activity_history.*
import kotlin.concurrent.thread

class HistoryRecordActivity : AppCompatActivity() {

    private val placeList = ArrayList<PlaceRecord>()

    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        initPlaceList()

        val size = placeList.size

        val layoutManager = LinearLayoutManager(this)
        historyRecord.layoutManager = layoutManager
        adapter = HistoryAdapter(this, placeList)
        historyRecord.adapter = adapter

        backBtn.setOnClickListener {
            onBackPressed()
        }

        /**
         * 删除数据库中保存信息
         */
        clearBtn.setOnClickListener {
            placeList.clear()
            deleteRecord()
            adapter.notifyDataSetChanged()
        }

    }

    private fun deleteRecord() {
        thread {
            Repository.deleteAll()
        }
    }

    private fun initPlaceList() {

        thread {
            val savedPlaces = Repository.getPlacesFromDatabase()
            placeList.addAll(savedPlaces)
            val size = placeList.size
        }

    }

}