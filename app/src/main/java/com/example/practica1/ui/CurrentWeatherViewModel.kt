package com.example.practica1.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.practica1.api_service.RetrofitClient
import com.example.practica1.data.CurrentWeatherData
import com.example.practica1.repository.FavoriteCitiesRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class CurrentWeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = RetrofitClient.weatherApiService
    private val favoriteRepo = FavoriteCitiesRepository(application.applicationContext)

    private val _weatherList = MutableLiveData<List<CurrentWeatherData>>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _error = MutableLiveData<String?>()

    val weatherList: LiveData<List<CurrentWeatherData>> = _weatherList
    val isLoading: LiveData<Boolean> = _isLoading
    val error: LiveData<String?> = _error

    fun loadWeatherForFavorites() {
        _isLoading.value = true
        _error.value = null

        val favoriteCities = favoriteRepo.getFavorites()
        if (favoriteCities.isEmpty()) {
            _error.value = "No hay ciudades favoritas guardadas"
            _isLoading.value = false
            return
        }

        viewModelScope.launch {
            try {
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

                val result = deferred.awaitAll().filterNotNull()

                if (result.isNotEmpty()) {
                    _weatherList.value = result
                } else {
                    _error.value = "No se pudieron cargar los datos del clima"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun removeFavorite(city: String) {
        favoriteRepo.removeFavorite(city)
        loadWeatherForFavorites()
    }
}