package com.skworks.knowyourweather.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.skworks.knowyourweather.viewmodel.WeatherViewModel

@OptIn(ExperimentalCoilApi::class)
@Composable
fun WeatherApp(navController: NavHostController) {
    val viewModel: WeatherViewModel = hiltViewModel()
    val lastSearchedCity by viewModel.lastSearchedCity.collectAsState()
    val city by viewModel.cityData.observeAsState()
    var cityName by remember { mutableStateOf("") }
    LaunchedEffect(city, lastSearchedCity) {
        cityName = city?.name ?: lastSearchedCity ?: ""
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = cityName,
            onValueChange = { cityName = it },
            label = { Text("Enter City") },
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            viewModel.saveLastSearchedCity(cityName)
            viewModel.fetchWeather(cityName)
        }) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.isLoading) {
            CircularProgressIndicator()
        } else {
            viewModel.weatherState?.let {
                Text("Temperature: ${it.main.temp}°C")
                Text("Humidity: ${it.main.humidity}%")
                Text("Condition: ${it.weather[0].description}")
                Image(
                    painter = rememberImagePainter("https://openweathermap.org/img/wn/${it.weather[0].icon}@2x.png"),
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(64.dp)
                )
            }

            viewModel.errorMessage?.let {
                Text("Error: $it", color = Color.Red)
            }
        }
    }
}
