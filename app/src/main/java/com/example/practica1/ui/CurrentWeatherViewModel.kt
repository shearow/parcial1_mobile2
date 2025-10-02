package com.example.practica1.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practica1.api_service.RetrofitClient
import com.example.practica1.api_service.WeatherApiService
import com.example.practica1.data.CurrentWeatherData
import com.example.practica1.repository.FavoriteCitiesRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class CurrentWeatherViewModel(
    private val favoriteCitiesRepository: FavoriteCitiesRepository
): ViewModel() {
    private val apiService = RetrofitClient.weatherApiService

    private val _weatherList = MutableLiveData<List<CurrentWeatherData>>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _error = MutableLiveData<String?>()

    val weatherList: LiveData<List<CurrentWeatherData>> get() = _weatherList
    val isLoading: LiveData<Boolean> get() = _isLoading
    val error: LiveData<String?> get() = _error


    //Load weather for all your favorite cities
    fun loadWeatherForFavorites() {
        val favoriteCities = favoriteCitiesRepository.getFavorites()
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            val deferred = favoriteCities.map { city ->
                async {
                    try {
                        val response = apiService.getCurrentWeather(city)
                        response.data.firstOrNull()
                    } catch (e: Exception) {
                        null
                    }
                }
            }

            val weatherDataList: List<CurrentWeatherData> = deferred.awaitAll().filterNotNull()
            _weatherList.value = weatherDataList
            _isLoading.value = false
        }
    }

    // Add new City and reload favorite cities
    fun addCityToFavorites(city: String) {
        favoriteCitiesRepository.addFavorite(city)
        loadWeatherForFavorites()
    }

    // Remove City to favorite cities
    fun removeCityFromFavorites(city: String) {
        favoriteCitiesRepository.removeFavorite(city)

        val currentList = _weatherList.value?.toMutableList() ?: mutableListOf()
        val index = currentList.indexOfFirst { it.city_name == city }
        if (index != -1) {
            currentList.removeAt(index)
            _weatherList.value = currentList
        }
    }

}