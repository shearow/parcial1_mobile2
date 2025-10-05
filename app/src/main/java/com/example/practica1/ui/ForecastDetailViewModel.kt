package com.example.practica1.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.practica1.data.DailyForecast
import com.example.practica1.data.DailyForecastResponse
import com.example.practica1.repository.FavoriteCitiesRepository

sealed class ForecastUiState {
    object Loading : ForecastUiState()
    data class Success(
        val forecastList: List<DailyForecast>,
        val mainDay: DailyForecast,
        val cityName: String,
        val countryCode: String,
        val isFavorite: Boolean
    ) : ForecastUiState()

    data class Error(val message: String) : ForecastUiState()
}

class ForecastDetailViewModel(
    private val favoriteRepo: FavoriteCitiesRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<ForecastUiState>()
    val uiState: LiveData<ForecastUiState> = _uiState

    private var forecastResponse: DailyForecastResponse? = null
    private var forecastList: List<DailyForecast> = emptyList()
    private var isFavorite = false

    fun loadForecast(response: DailyForecastResponse?) {
        if (response == null) {
            _uiState.value = ForecastUiState.Error("No forecast data received")
            return
        }

        forecastResponse = response
        forecastList = response.data

        val cityName = response.city_name
        isFavorite = favoriteRepo.isFavorite(cityName)

        _uiState.value = ForecastUiState.Success(
            forecastList = forecastList,
            mainDay = forecastList.first(),
            cityName = cityName,
            countryCode = response.country_code,
            isFavorite = isFavorite
        )
    }

    fun selectDay(position: Int) {
        forecastList.getOrNull(position)?.let { day ->
            val city = forecastResponse?.city_name ?: ""
            val country = forecastResponse?.country_code ?: ""

            _uiState.value = ForecastUiState.Success(
                forecastList = forecastList,
                mainDay = day,
                cityName = city,
                countryCode = country,
                isFavorite = isFavorite
            )
        }
    }

    fun toggleFavorite() {
        val city = forecastResponse?.city_name ?: return

        if (isFavorite) {
            favoriteRepo.removeFavorite(city)
            isFavorite = false
        } else {
            favoriteRepo.addFavorite(city)
            isFavorite = true
        }

        _uiState.value = (_uiState.value as? ForecastUiState.Success)?.copy(
            isFavorite = isFavorite
        )
    }
}