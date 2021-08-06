package com.example.simpleweatherapp.data.model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Clouds(
    val all: Int
) : Parcelable
