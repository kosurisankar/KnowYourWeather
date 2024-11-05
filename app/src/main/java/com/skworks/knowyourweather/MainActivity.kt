package com.skworks.knowyourweather

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.skworks.knowyourweather.ui.components.WeatherApp
import com.skworks.knowyourweather.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var locationPermissionLauncher: ActivityResultLauncher<String>
    private val locationViewModel: WeatherViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize permission launcher
        locationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                locationViewModel.fetchLocation()
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
        requestLocationPermission()
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "search") {
                composable("search") { WeatherApp(navController) }
            }
        }
    }

    fun requestLocationPermission() {
        if (::locationPermissionLauncher.isInitialized) {
            locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            Log.e("MainActivity", "Location permission launcher not initialized")
        }
    }

}
