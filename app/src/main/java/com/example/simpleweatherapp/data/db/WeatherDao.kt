package com.example.simpleweatherapp.data.db

import androidx.room.*
import com.example.simpleweatherapp.data.model.Weather

@Dao
interface WeatherDao {
    @Query("SELECT * FROM cities")
    suspend fun getLocalCities(): List<Weather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(weather: Weather)

    @Delete
    suspend fun deleteCity(weather: Weather)

    @Query("SELECT EXISTS(SELECT * FROM cities WHERE name = :name)")
    fun isRowIsExist(name: String): Boolean
}
