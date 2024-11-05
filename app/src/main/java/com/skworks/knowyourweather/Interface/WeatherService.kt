package com.skworks.knowyourweather.Interface

import com.skworks.knowyourweather.data.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather")
    suspend fun getWeatherByCity(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ): WeatherResponse

}