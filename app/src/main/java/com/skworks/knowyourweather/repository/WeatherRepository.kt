package com.skworks.knowyourweather.repository

import com.skworks.knowyourweather.Interface.WeatherService
import com.skworks.knowyourweather.data.WeatherResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(private val service: WeatherService) {
    suspend fun fetchWeather(city: String, apiKey: String): WeatherResponse {
        return service.getWeatherByCity(city, apiKey)
    }
}