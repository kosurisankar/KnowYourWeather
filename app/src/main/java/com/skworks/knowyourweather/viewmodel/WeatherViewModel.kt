package com.skworks.knowyourweather.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skworks.knowyourweather.DataStoreHelper
import com.skworks.knowyourweather.data.WeatherResponse
import com.skworks.knowyourweather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val dataStoreHelper: DataStoreHelper,
    private val repository: WeatherRepository
) : ViewModel() {
    // Expose last searched city to the UI
    private val _lastSearchedCity = MutableStateFlow<String?>("")
    val lastSearchedCity: StateFlow<String?> = _lastSearchedCity

    init {
        viewModelScope.launch {
            dataStoreHelper.lastSearchedCityFlow.collect { city ->
                _lastSearchedCity.value = city
            }
        }
    }

    fun saveLastSearchedCity(city: String) {
        viewModelScope.launch {
            dataStoreHelper.saveLastSearchedCity(city)
        }
    }
    var weatherState by mutableStateOf<WeatherResponse?>(null)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun fetchWeather(city: String, apiKey: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                weatherState = repository.fetchWeather(city, apiKey)
//                Log.e("Response:::::",weatherState.toString())
            } catch (e: Exception) {
                errorMessage = "Error fetching weather data"
            }
            isLoading = false
        }
    }
}
