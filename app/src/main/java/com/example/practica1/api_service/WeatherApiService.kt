package com.example.practica1.api_service

import com.example.practica1.Config
import com.example.practica1.data.CurrentWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApiService {

    @GET("current")
    suspend fun getCurrentWeather(
        @Query("city") city: String,
        @Query("key") apiKey: String = Config.WEATHERBIT_API_KEY
    ): CurrentWeatherResponse

}