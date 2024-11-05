//package com.skworks.knowyourweather
//
//// WeatherViewModelTest.kt
//import androidx.lifecycle.Observer
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.skworks.knowyourweather.model.CityResponse
//import com.skworks.knowyourweather.model.Main
//import com.skworks.knowyourweather.model.Weather
//import com.skworks.knowyourweather.model.WeatherResponse
//import com.skworks.knowyourweather.repository.WeatherRepository
//import com.skworks.knowyourweather.utils.API_KEY
//import com.skworks.knowyourweather.viewmodel.WeatherViewModel
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.test.StandardTestDispatcher
//import kotlinx.coroutines.test.TestScope
//import kotlinx.coroutines.test.setMain
//import kotlinx.coroutines.test.runTest
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.mockito.Mock
//import org.mockito.Mockito.*
//import org.mockito.MockitoAnnotations
//import org.junit.Assert.*
//
//@OptIn(ExperimentalCoroutinesApi::class)
//class WeatherViewModelTest {
//
//    @get:Rule
//    val instantExecutorRule = InstantTaskExecutorRule()
//
//    private lateinit var viewModel: WeatherViewModel
//    private val testDispatcher = StandardTestDispatcher()
//    private val testScope = TestScope(testDispatcher)
//
//    @Mock
//    private lateinit var dataStoreHelper: DataStoreHelper
//
//    @Mock
//    private lateinit var repository: WeatherRepository
//
//    @Mock
//    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
//
//    @Mock
//    private lateinit var cityObserver: Observer<CityResponse>
//
//    @Mock
//    private lateinit var weatherObserver: Observer<WeatherResponse?>
//
//    @Before
//    fun setUp() {
//        MockitoAnnotations.openMocks(this)
//        Dispatchers.setMain(testDispatcher)
//
//        // Mock the last searched city flow
//        `when`(dataStoreHelper.lastSearchedCityFlow).thenReturn(MutableStateFlow("New York"))
//
//        viewModel = WeatherViewModel(dataStoreHelper, repository, fusedLocationProviderClient)
//    }
//
//    @Test
//    fun `test fetchWeather updates weatherState`() = testScope.runTest {
//        // Arrange
//        val weatherResponse = WeatherResponse()
//        `when`(repository.fetchWeather("New York", API_KEY)).thenReturn(weatherResponse)
//
//        viewModel.weatherState = weatherResponse
//        viewModel.weatherState.observeForever(weatherObserver)
//
//        // Act
//        viewModel.fetchWeather("New York")
//        testDispatcher.scheduler.advanceUntilIdle() // Wait for coroutine
//
//        // Assert
//        verify(weatherObserver).onChanged(weatherResponse)
//        assertEquals(weatherResponse, viewModel.weatherState.value)
//    }
//
//    @Test
//    fun `test fetchLocation calls getCityName with correct coordinates`() = testScope.runTest {
//        // Arrange
//        val mockLatitude = 40.7128
//        val mockLongitude = -74.0060
//        val mockCityResponse = CityResponse("New York")
//        `when`(fusedLocationProviderClient.lastLocation).thenReturn(mockLocation)
//        `when`(repository.fetchCityByCoordinates(mockLatitude, mockLongitude, API_KEY)).thenReturn(listOf(mockCityResponse))
//
//        viewModel.cityData.observeForever(cityObserver)
//
//        // Act
//        viewModel.fetchLocation()
//        testDispatcher.scheduler.advanceUntilIdle() // Wait for coroutine
//
//        // Assert
//        verify(cityObserver).onChanged(mockCityResponse)
//        assertEquals(mockCityResponse, viewModel.cityData.value)
//    }
//}
