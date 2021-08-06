package com.example.simpleweatherapp.adapters

import com.example.simpleweatherapp.data.model.Weather

interface WeatherItemClickListener {
    fun onClickedWeather(weather: Weather)
}
