package com.example.practica1

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
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


        val iconUrl = "https://www.weatherbit.io/static/img/icons/${day.weather.icon}.png"
        Picasso.get().load(iconUrl).into(binding.imgMainWeatherIcon)
    }
}