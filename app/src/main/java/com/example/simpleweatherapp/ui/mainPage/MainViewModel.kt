package com.example.simpleweatherapp.ui.mainPage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleweatherapp.data.model.Weather
import com.example.simpleweatherapp.data.repository.WeatherRepository
import com.example.simpleweatherapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    val localCities: MutableLiveData<List<Weather>> = MutableLiveData()

    val weatherByLocationResponse: MutableLiveData<Resource<Weather>> = MutableLiveData()
    val weatherByCityResponse: MutableLiveData<Resource<Weather>> = MutableLiveData()

    private val ioScope = CoroutineScope(Dispatchers.IO)

    fun getWeatherByLocation(lat: Double, lon: Double) = ioScope.launch {
        weatherByLocationResponse.postValue(Resource.loading())
        val responseByLocation = weatherRepository.getWeatherByLocation(lat, lon)

        if (responseByLocation.status == Resource.Status.SUCCESS) {
            weatherByLocationResponse.postValue(Resource.success(responseByLocation.data!!))
        } else {
            weatherByLocationResponse.postValue(Resource.error(responseByLocation.message))
        }
    }

    fun getWeatherByCity(cityName: String) = viewModelScope.launch {
        weatherByCityResponse.postValue(Resource.loading())
        val responseByCity = weatherRepository.getWeatherByCity(cityName)

        if (responseByCity.status == Resource.Status.SUCCESS) {
            weatherByCityResponse.postValue(Resource.success(responseByCity.data!!))
        } else {
            weatherByCityResponse.postValue(Resource.error(responseByCity.message))
        }
    }

    fun getCitiesFromLocal() = ioScope.launch {
        localCities.postValue(weatherRepository.getLocalCities())
    }

    fun insertCity(weather: Weather) = viewModelScope.launch {
        weatherRepository.insertCity(weather)
    }

    fun removeCity(weather: Weather) = viewModelScope.launch {
        weatherRepository.deleteCity(weather)
    }
}
