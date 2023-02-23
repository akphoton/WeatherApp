package com.example.weatherapp.api

import com.example.weatherapp.WeatherData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("/data/2.5/weather?units=metric&appid=89d736713667b5ea6cf9a3e85f80db44")
    suspend fun getWeather(@Query("q") query: String) : Response<WeatherData>

}