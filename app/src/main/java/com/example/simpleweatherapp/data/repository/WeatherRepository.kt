package com.example.simpleweatherapp.data.repository

import com.example.simpleweatherapp.data.api.RemoteDataSource
import com.example.simpleweatherapp.data.db.WeatherDao
import com.example.simpleweatherapp.data.model.Weather
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: WeatherDao
) {
    suspend fun getWeatherByLocation(lat: Double, lon: Double) =
        remoteDataSource.getWeatherByLocation(lat, lon)

    suspend fun getWeatherByCity(cityName: String) =
        remoteDataSource.getWeatherByCity(cityName)

    suspend fun getLocalCities() =
        localDataSource.getLocalCities()

    suspend fun insertCity(weather: Weather) =
        localDataSource.insertCity(weather)

    suspend fun deleteCity(weather: Weather) =
        localDataSource.deleteCity(weather)
}
