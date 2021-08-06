package com.example.simpleweatherapp.data.api

import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val weatherService: WeatherService
) : BaseDataSource() {

    suspend fun getWeatherByLocation(lat: Double, lon: Double) =
        getResult { weatherService.getWeatherByLocation(lat, lon) }

    suspend fun getWeatherByCity(cityName: String) =
        getResult { weatherService.getWeatherByCity(cityName) }
}
