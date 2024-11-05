package com.skworks.knowyourweather.model

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>
)

data class Main(
    val temp: Float,
    val humidity: Int
)

data class Weather(
    val description: String,
    val icon: String
)

data class CityResponse(
    val name: String
)
