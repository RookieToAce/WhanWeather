package com.example.whanweather.logic.weatherdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.whanweather.logic.dao.HistoryDao
import com.example.whanweather.logic.dao.PlaceDao
import com.example.whanweather.logic.entity.PlaceRecord

@Database(version = 1, entities = [PlaceRecord::class])
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun HistoryDao(): HistoryDao

    companion object {

        private var instance: WeatherDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): WeatherDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(
                context.applicationContext,
                WeatherDatabase::class.java,
                "weather_database"
            ).build().apply {
                instance = this
            }
        }

    }

}