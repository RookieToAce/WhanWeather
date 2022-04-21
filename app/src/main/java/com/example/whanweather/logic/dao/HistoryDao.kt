package com.example.whanweather.logic.dao

import androidx.room.*
import com.example.whanweather.logic.entity.PlaceRecord

//
@Dao
interface HistoryDao {

    @Insert
    fun insertPlace(placeRecord: PlaceRecord): Long

    @Update
    fun updatePlace(newPlace: PlaceRecord)

    @Query("select * from PlaceRecord")
    fun loadAllPlaces(): List<PlaceRecord>

    @Query("select * from PlaceRecord where name = :name ")
    fun getPlaceFormName(name: String): PlaceRecord

    @Query("select count(*) from PlaceRecord where name = :name limit 1")
    fun containPlace(name: String): Boolean

    @Delete
    fun delete(place: PlaceRecord)

    @Query("delete from PlaceRecord")
    fun deleteAll()

}