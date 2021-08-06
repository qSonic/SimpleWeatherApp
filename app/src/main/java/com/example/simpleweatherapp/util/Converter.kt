package com.example.simpleweatherapp.util

import androidx.room.TypeConverter
import com.example.simpleweatherapp.data.model.WeatherX
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class Converter {
    private val gson = Gson()

    @TypeConverter
    fun stringToSomeObjectList(weatherX: String?): List<WeatherX?>? {
        val listType: Type = object : TypeToken<List<WeatherX?>?>() {}.type
        return gson.fromJson<List<WeatherX?>>(weatherX, listType)
    }
    @TypeConverter
    fun fromWeatherToString(weatherX: List<WeatherX>): String? {
        return gson.toJson(weatherX)
    }
}
