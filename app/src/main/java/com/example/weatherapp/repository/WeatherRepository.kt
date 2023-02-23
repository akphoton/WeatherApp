package com.example.weatherapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.WeatherData
import com.example.weatherapp.api.WeatherService

class WeatherRepository(val weatherService: WeatherService) {

    private val weatherLiveData = MutableLiveData<WeatherData>();

    val weather: LiveData<WeatherData>
    get() = weatherLiveData

    suspend fun getWeather(query: String){
        val result = weatherService.getWeather(query)
        if(result.body()!=null){
            weatherLiveData.postValue(result.body())
        }

    }

}