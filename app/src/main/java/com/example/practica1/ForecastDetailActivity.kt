package com.example.practica1

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practica1.adapter.ForecastAdapter
import com.example.practica1.data.DailyForecastResponse
import com.example.practica1.databinding.ActivityForecastDetailBinding
import com.example.practica1.repository.FavoriteCitiesRepository
import com.example.practica1.ui.ForecastDetailViewModel
import com.example.practica1.ui.ForecastUiState
import com.squareup.picasso.Picasso

class ForecastDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForecastDetailBinding
    private lateinit var adapter: ForecastAdapter

    private val viewModel: ForecastDetailViewModel by viewModels {
        val repo = FavoriteCitiesRepository(this)
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return ForecastDetailViewModel(repo) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForecastDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecycler()
        setupListeners()

        val forecastData = intent.getSerializableExtra("forecastData") as? DailyForecastResponse
        viewModel.loadForecast(forecastData)

        viewModel.uiState.observe(this) { state ->
            when (state) {
                is ForecastUiState.Loading -> { /* opcional: mostrar ProgressBar */ }
                is ForecastUiState.Error -> { Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show() }
                is ForecastUiState.Success -> { updateUI(state) }
            }
        }
    }

    private fun setupRecycler() {
        adapter = ForecastAdapter(emptyList()) { position ->
            viewModel.selectDay(position)
        }
        binding.rvForecastDays.adapter = adapter
        binding.rvForecastDays.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { finish() }
        binding.btnFavorite.setOnClickListener { viewModel.toggleFavorite() }
    }

    private fun updateUI(state: ForecastUiState.Success) {
        adapter.updateData(state.forecastList)

        val day = state.mainDay
        binding.tvCityAndCountry.text = "${state.cityName}, ${state.countryCode}"
        binding.tvMainDayName.text = getDayOfWeek(day.valid_date)
        binding.tvMainWeatherDescription.text = day.weather.description
        binding.tvMainTempMax.text = "${day.max_temp.toInt()}°C"
        binding.tvMainTempMin.text = "${day.min_temp.toInt()}°C"
        binding.tvPrecipitationPorc.text = "${day.pop}% / ${String.format("%.2f", day.precip)}mm"
        binding.tvHumidity.text = "Humidity: ${day.rh}%"
        binding.tvWind.text = "Wind: ${day.wind_spd} m/s"

        updateFavoriteIcon(state.isFavorite)
        updateBackgroundForWeather(day.weather.icon)

        val iconUrl = "https://www.weatherbit.io/static/img/icons/${day.weather.icon}.png"
        Picasso.get().load(iconUrl).into(binding.imgMainWeatherIcon)
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        binding.btnFavorite.imageTintList = ColorStateList.valueOf(if (isFavorite) Color.RED else Color.WHITE)
    }

    private fun updateBackgroundForWeather(weatherIcon: String) {
        val backgroundRes = when (weatherIcon) {
            "c01d", "c01n" -> R.drawable.bg_clear
            "c02d", "c02n" -> R.drawable.bg_clear
            "c03d", "c03n", "c04d", "c04n" -> R.drawable.bg_cloudy
            "t01d", "t01n", "t02d", "t02n", "t03d", "t03n",
            "t04d", "t04n", "t05d", "t05n" -> R.drawable.bg_storm
            "d01d", "d01n", "d02d", "d02n", "d03d", "d03n",
            "r01d", "r01n", "r02d", "r02n", "r03d", "r03n",
            "r04d", "r04n", "r05d", "r05n", "r06d", "r06n",
            "f01d", "f01n" -> R.drawable.bg_cloudy
            "s01d", "s01n", "s02d", "s02n", "s03d", "s03n",
            "s04d", "s04n", "s05d", "s05n", "s06d", "s06n" -> R.drawable.bg_snow
            else -> R.drawable.bg_clear
        }
        binding.root.setBackgroundResource(backgroundRes)
    }
}