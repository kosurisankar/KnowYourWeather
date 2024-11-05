package com.skworks.knowyourweather

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Create an extension to get the DataStore instance
val Context.dataStore by preferencesDataStore(name = "weather_preferences")

class DataStoreHelper(private val context: Context) {

    private val LAST_SEARCHED_CITY_KEY = stringPreferencesKey("last_searched_city")

    // Retrieve last searched city
    val lastSearchedCityFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[LAST_SEARCHED_CITY_KEY]
        }

    // Save last searched city
    suspend fun saveLastSearchedCity(city: String) {
        context.dataStore.edit { preferences ->
            preferences[LAST_SEARCHED_CITY_KEY] = city
        }
    }
}
