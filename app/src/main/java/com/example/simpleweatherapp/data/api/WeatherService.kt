package com.example.simpleweatherapp.data.api

import com.example.simpleweatherapp.data.model.Weather
import com.example.simpleweatherapp.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    suspend fun getWeatherByLocation(@Query("lat") lat: Double,
                                     @Query("lon") lon: Double,
                                     @Query("appid") appid: String = API_KEY) : Response<Weather>

}
