package com.skworks.knowyourweather.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.skworks.knowyourweather.DataStoreHelper
import com.skworks.knowyourweather.model.CityResponse
import com.skworks.knowyourweather.model.WeatherResponse
import com.skworks.knowyourweather.repository.WeatherRepository
import com.skworks.knowyourweather.utils.API_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val dataStoreHelper: DataStoreHelper,
    private val repository: WeatherRepository,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : ViewModel() {

    private val _cityData = MutableLiveData<CityResponse>()
    val cityData: LiveData<CityResponse> = _cityData

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

    // Method to fetch location
    @SuppressLint("MissingPermission")
    fun fetchLocation() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
//                Log.e("LONG and LAT-----------",""+it.latitude+"===="+it.longitude)
                getCityName(it.latitude, it.longitude)
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

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                weatherState = repository.fetchWeather(city, API_KEY)
//                Log.e("Response:::::",weatherState.toString())
            } catch (e: Exception) {
                errorMessage = "Error fetching weather data"
            }
            isLoading = false
        }
    }

    fun getCityName(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = repository.fetchCityByCoordinates(latitude, longitude, API_KEY)
                if (response.isNotEmpty()) {
                    _cityData.value = response.first()
                }
//                Log.e("Response:::cityData::",":.......::"+cityData.toString())
            } catch (e: Exception) {
                errorMessage = "Error fetching weather data"
            }
            isLoading = false
        }
    }
}
