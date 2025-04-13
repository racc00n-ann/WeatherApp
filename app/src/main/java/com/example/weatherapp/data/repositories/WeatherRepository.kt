package com.example.weatherapp.data.repositories

import com.example.weatherapp.data.api.WeatherApi
import com.example.weatherapp.data.models.WeatherResponse


class WeatherRepository(private val api: WeatherApi)  {
    suspend fun getWeather(latitude: Double, longitude: Double): WeatherResponse = api.getWeather(latitude, longitude)
}