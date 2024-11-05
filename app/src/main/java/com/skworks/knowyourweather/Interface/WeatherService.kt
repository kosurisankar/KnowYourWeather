package com.skworks.knowyourweather.Interface

import com.skworks.knowyourweather.model.CityResponse
import com.skworks.knowyourweather.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/weather")
    suspend fun getWeatherByCity(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ): WeatherResponse

    // Get city data by latitude and longitude
    @GET("geo/1.0/reverse")
    suspend fun getCityByCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("limit") limit: Int = 1,
        @Query("appid") apiKey: String
    ): List<CityResponse>
}