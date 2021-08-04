package com.example.simpleweatherapp.data.api

import com.example.simpleweatherapp.data.BaseSource
import javax.inject.Inject

class RemoteSource @Inject constructor(
    private val weatherService: WeatherService
) : BaseSource() {

    suspend fun getWeatherByLocation(lat: Double, lon: Double) =
        getResult { weatherService.getWeatherByLocation(lat, lon) }

}