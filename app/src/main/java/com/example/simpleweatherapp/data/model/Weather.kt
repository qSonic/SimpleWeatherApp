package com.example.simpleweatherapp.data.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "cities"
)
@Parcelize
data class Weather(
    var base: String,
    @Embedded
    var clouds: Clouds,
    var cod: Int,
    @Embedded
    var coord: Coord,
    var dt: Int,
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @Embedded
    var main: Main,
    var name: String,
    @Embedded
    var sys: Sys,
    var timezone: Int,
    var weather: List<WeatherX>,
    @Embedded
    var wind: Wind
) : Parcelable
