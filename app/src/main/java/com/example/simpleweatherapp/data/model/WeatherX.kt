package com.example.simpleweatherapp.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class WeatherX(
    val description: String,
    val icon: String,
    @ColumnInfo(name = "weatherX_id")
    val id: Int,
    val main: String
) : Parcelable
