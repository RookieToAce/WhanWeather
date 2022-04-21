package com.example.whanweather.logic.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlaceRecord(val name: String, var temperature: String, var sky: String) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

}


