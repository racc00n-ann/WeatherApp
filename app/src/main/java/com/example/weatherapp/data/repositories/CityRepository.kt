package com.example.weatherapp.data.repositories

import com.example.weatherapp.data.api.CityApi
import com.example.weatherapp.data.models.CityFromNet

class CityRepository(private val cityApi: CityApi) {
    suspend fun getCityByName (name: String): List<CityFromNet> = cityApi.getCity(name)
}