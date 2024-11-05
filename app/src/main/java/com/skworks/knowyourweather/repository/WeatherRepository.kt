package com.skworks.knowyourweather.repository

import com.skworks.knowyourweather.Interface.WeatherService
import com.skworks.knowyourweather.model.CityResponse
import com.skworks.knowyourweather.model.WeatherResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(private val service: WeatherService) {
    suspend fun fetchWeather(city: String, apiKey: String): WeatherResponse {
        return service.getWeatherByCity(city, apiKey)
    }

    suspend fun fetchCityByCoordinates(latitude: Double, longitude: Double, apiKey: String): List<CityResponse> {
        return service.getCityByCoordinates(latitude, longitude, apiKey = apiKey)
    }
}