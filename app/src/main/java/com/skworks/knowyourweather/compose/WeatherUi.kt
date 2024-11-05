package com.skworks.knowyourweather.compose

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.skworks.knowyourweather.viewmodel.WeatherViewModel

@OptIn(ExperimentalCoilApi::class)
@Composable
fun WeatherApp(navController: NavHostController) {
    val viewModel: WeatherViewModel = hiltViewModel()
    val lastSearchedCity by viewModel.lastSearchedCity.collectAsState()

    var cityName by remember { mutableStateOf("") }
    LaunchedEffect(lastSearchedCity) {
        // Update searchQuery whenever lastSearchedCity is updated
        cityName = lastSearchedCity ?: ""
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp, start = 16.dp, end = 16.dp),
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
            viewModel.fetchWeather(cityName, "713e9e1b200a1dcfce03c4f852503e77")
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

@Preview
@Composable
fun ShowWeatherApp() {

    WeatherApp(rememberNavController())
}