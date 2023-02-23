package com.example.weatherapp.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.WeatherData
import com.example.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(val repository: WeatherRepository, defaultLocation: String) : ViewModel() {
    init {
        getWeather(defaultLocation)
    }
    fun getWeather(location: String){
        viewModelScope.launch( Dispatchers.IO) {
            repository.getWeather(location)
        }
    }

    val weather : LiveData<WeatherData>
    get() = repository.weather
}