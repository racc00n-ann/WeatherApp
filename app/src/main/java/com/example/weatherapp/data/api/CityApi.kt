package com.example.weatherapp.data.api

import com.example.weatherapp.data.models.CityFromNet
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CityApi {
    @GET("v1/city")
    suspend fun getCity(
        @Query("name") name: String,
        @Header("X-Api-Key") api: String = "mH2p2S2MgKYYw17Bn0FdcQ==0bUzgGelnC96YKPW"
    ): List<CityFromNet>
}