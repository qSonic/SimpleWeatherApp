package com.example.simpleweatherapp.data.model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Coord(
    val lat: Double,
    val lon: Double
) : Parcelable
