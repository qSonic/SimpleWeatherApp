package com.example.simpleweatherapp.data.repository

import com.example.simpleweatherapp.data.api.RemoteSource
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val remoteDataSource: RemoteSource
) {
    suspend fun getWeatherByLocation(lat: Double, lon: Double) = remoteDataSource.getWeatherByLocation(lat, lon)
}