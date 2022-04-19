package com.example.whanweather.logic.model

import java.text.SimpleDateFormat
import java.util.*

object TimeUnits {

    fun getNowTime(): String {
        val simpleDataFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val date = Date(System.currentTimeMillis())
        return simpleDataFormat.format(date)
    }

}