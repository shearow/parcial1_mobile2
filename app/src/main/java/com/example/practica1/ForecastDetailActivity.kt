package com.example.practica1

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practica1.adapter.ForecastAdapter
import com.example.practica1.data.DailyForecast
import com.example.practica1.data.DailyForecastResponse
import com.example.practica1.databinding.ActivityForecastDetailBinding
import com.example.practica1.repository.FavoriteCitiesRepository
import com.squareup.picasso.Picasso

class ForecastDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForecastDetailBinding
    private lateinit var adapter: ForecastAdapter
    private lateinit var favoriteRepo: FavoriteCitiesRepository
    private var forecastResponse: DailyForecastResponse? = null
    private var forecastList: List<DailyForecast> = emptyList()
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForecastDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoriteRepo = FavoriteCitiesRepository(this)

        val forecastData = intent.getSerializableExtra("forecastData") as? DailyForecastResponse
        forecastData?.let {
            forecastResponse = it
            forecastList = it.data
            setupRecycler()
            showMainDay(0)
        }

        setupFavoriteButton()
        setupListeners()
    }

    private fun setupFavoriteButton() {
        val cityName = forecastResponse?.city_name ?: return

        isFavorite = favoriteRepo.isFavorite(cityName)
        updateFavoriteIcon()

        binding.btnFavorite.setOnClickListener {
            if (isFavorite) {
                favoriteRepo.removeFavorite(cityName)
                isFavorite = false
                Toast.makeText(this, "$cityName delete to favorites", Toast.LENGTH_SHORT).show()
            } else {
                favoriteRepo.addFavorite(cityName)
                isFavorite = true
                Toast.makeText(this, "$cityName add to favorites", Toast.LENGTH_SHORT).show()
            }
            updateFavoriteIcon()
        }
    }

    private fun updateFavoriteIcon() {
        if (isFavorite) {
            binding.btnFavorite.imageTintList = ColorStateList.valueOf(Color.RED)
        } else {
            binding.btnFavorite.imageTintList = ColorStateList.valueOf(Color.WHITE)
        }
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupRecycler() {
        adapter = ForecastAdapter(forecastList) { position ->
            showMainDay(position)
        }

        binding.rvForecastDays.adapter = adapter
        binding.rvForecastDays.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun showMainDay(position: Int) {
        val day = forecastList[position]

        val dayText = if (position == 0) {
            "Today"
        } else {
            getDayOfWeek(day.valid_date)
        }
        binding.tvMainDayName.text = dayText
        forecastResponse?.let {
            binding.tvCityAndCountry.text = "${it.city_name}, ${it.country_code}"
        }
        binding.tvMainWeatherDescription.text = day.weather.description
        binding.tvMainTempMax.text = "${day.max_temp.toInt()}°C"
        binding.tvMainTempMin.text = "${day.min_temp.toInt()}°C"
        binding.tvPrecipitation.text = "Precipitation"
        binding.tvPrecipitationPorc.text = "${day.pop}% / ${String.format("%.2f", day.precip)}mm"
        binding.tvHumidity.text = "Humidity: ${day.rh}%"
        binding.tvWind.text = "Wind: ${day.wind_spd} m/s"
        updateBackgroundForWeather(day.weather.icon)

        val iconUrl = "https://www.weatherbit.io/static/img/icons/${day.weather.icon}.png"
        Picasso.get().load(iconUrl).into(binding.imgMainWeatherIcon)

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