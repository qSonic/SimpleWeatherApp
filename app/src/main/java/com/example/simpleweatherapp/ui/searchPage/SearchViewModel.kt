package com.example.simpleweatherapp.ui.searchPage

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleweatherapp.data.model.Weather
import com.example.simpleweatherapp.data.repository.WeatherRepository
import com.example.simpleweatherapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    val citiesList: MutableLiveData<ArrayList<Weather>> = MutableLiveData()
    val citiesResponse: MutableLiveData<Resource<Weather>> = MutableLiveData()

    fun getWeather(lat: Double, lon: Double) = viewModelScope.launch {

        citiesResponse.postValue(Resource.loading())
        val response = weatherRepository.getWeatherByLocation(lat, lon)

        if (response.status == Resource.Status.SUCCESS){
            citiesResponse.postValue(Resource.success(response.data!!))
        } else {
            citiesResponse.postValue(Resource.error(response.message))
        }
    }




}
