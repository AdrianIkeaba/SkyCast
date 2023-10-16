package com.ghostdev.skycast.presentation.screens

import android.animation.ArgbEvaluator
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.ghostdev.skycast.BuildConfig
import com.ghostdev.skycast.R
import com.ghostdev.skycast.api.WeatherServiceImpl
import com.ghostdev.skycast.databinding.ActivityMainBinding
import com.ghostdev.skycast.datasource.WeatherDataSourceImpl
import com.ghostdev.skycast.model.AstronomyData
import com.ghostdev.skycast.model.CurrentWeather
import com.ghostdev.skycast.presentation.WeatherViewModel
import com.ghostdev.skycast.presentation.WeatherViewModelFactory
import com.ghostdev.skycast.repo.WeatherRepo
import com.ghostdev.skycast.util.InternetAvailability
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var factory: WeatherViewModelFactory
    private lateinit var viewModel: WeatherViewModel
    private lateinit var binding: ActivityMainBinding
    private var location: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.light_blue)

        location = intent.getStringExtra("location").toString()

        val weatherServiceImpl = WeatherServiceImpl(BuildConfig.BASE_URL)
        val weatherDataSource = WeatherDataSourceImpl(weatherServiceImpl, BuildConfig.API_KEY, location)
        val weatherRepo = WeatherRepo(weatherDataSource)

        factory = WeatherViewModelFactory(weatherRepo)
        viewModel = ViewModelProvider(this, factory) [WeatherViewModel::class.java]

        if (InternetAvailability().isInternetAvailable(this)) {
            binding.swiperefresh.isRefreshing = true
            getCurrentWeatherData()
            getAstronomyData()
        } else {
            Toast.makeText(this@MainActivity, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
        setBackgroundAnimation(binding.scrollView, binding.cardConst1, binding.cardConst2)


        binding.swiperefresh.setOnRefreshListener {
            if (InternetAvailability().isInternetAvailable(this)) {
                getCurrentWeatherData()
                getAstronomyData()
            } else {
                Toast.makeText(this@MainActivity, "No Internet Connection", Toast.LENGTH_SHORT).show()
                binding.swiperefresh.isRefreshing = false
            }
        }

/*        //Forecast xml init
        //Monday
        val mondayText = findViewById<TextView>(R.id.monday)
        val mondayWeather = findViewById<ImageView>(R.id.mondayWeather)
        val mondayTemp = findViewById<TextView>(R.id.mondayTemp)

        //Tuesday
        val tuesdayText = findViewById<TextView>(R.id.tuesday)
        val tuesdayWeather = findViewById<ImageView>(R.id.tuesdayWeather)
        val tuesdayTemp = findViewById<TextView>(R.id.tuesdayTemp)

        //Wednesday
        val wednesdayText = findViewById<TextView>(R.id.wednesday)
        val wednesdayWeather = findViewById<ImageView>(R.id.wednesdayWeather)
        val wednesdayTemp = findViewById<TextView>(R.id.wednesdayTemp)

        //Thursday
        val thursdayText = findViewById<TextView>(R.id.thursday)
        val thursdayWeather = findViewById<ImageView>(R.id.thursdayWeather)
        val thursdayTemp = findViewById<TextView>(R.id.thursdayTemp)

        //Friday
        val fridayText = findViewById<TextView>(R.id.friday)
        val fridayWeather = findViewById<ImageView>(R.id.fridayWeather)
        val fridayTemp = findViewById<TextView>(R.id.fridayTemp)

        //Saturday
        val saturdayText = findViewById<TextView>(R.id.saturday)
        val saturdayWeather = findViewById<ImageView>(R.id.saturdayWeather)
        val saturdayTemp = findViewById<TextView>(R.id.saturdayTemp)

        //Sunday
        val sundayText = findViewById<TextView>(R.id.sunday)
        val sundayWeather = findViewById<ImageView>(R.id.sundayWeather)
        val sundayTemp = findViewById<TextView>(R.id.sundayTemp)
        */


    }

    private fun setBackgroundAnimation(nestedScrollView: NestedScrollView, cardConst1: ConstraintLayout, cardConst2: ConstraintLayout) {

        val evaluator = ArgbEvaluator()

        val colorStart = this.getColor(R.color.light_blue)
        val colorEnd = Color.BLACK

        val colorStart2 = this.getColor(R.color.dark_blue)
        val colorEnd2 = Color.BLACK

        var progress: Float
        val velocity = 3

        nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            val scrollViewHeight = nestedScrollView.height

            if (scrollViewHeight > 0) {
                progress = (scrollY / scrollViewHeight.toFloat()) * velocity
                nestedScrollView.setBackgroundColor(evaluator.evaluate(progress, colorStart, colorEnd) as Int)
                cardConst1.setBackgroundColor(evaluator.evaluate(progress, colorStart2, colorEnd2) as Int)
                cardConst2.setBackgroundColor(evaluator.evaluate(progress, colorStart2, colorEnd2) as Int)

            }
        }
        nestedScrollView.viewTreeObserver.addOnScrollChangedListener {
            val scrollY = nestedScrollView.scrollY
            val maxScrollY = nestedScrollView.getChildAt(0).height - nestedScrollView.height
            val eightyPercentOfMaxScrollY = (0.4 * maxScrollY).toInt()

            if (scrollY >= eightyPercentOfMaxScrollY) {
                // The ScrollView has reached the end
                cardConst1.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_grey))
                cardConst2.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_grey))

            }

        }

    }
    // Define the bind function as an extension function
    @SuppressLint("SetTextI18n")
    private fun CurrentWeather.bind() {
        binding.tempText.text = current.tempCelsius.toInt().toString() + "°"
        binding.currentLocation.text = location.name + ", " + location.region
        binding.weatherDesc.text = current.condition.weatherCondition
        binding.dateText.text = convertDate(location.localTime)
        if (current.uv < 2.0) {
            binding.uvText.text = "Low"
        } else if (current.uv in 3.0..5.0) {
            binding.uvText.text = "Moderate"
        } else if (current.uv in 6.0..7.0) {
            binding.uvText.text = "High"
        } else if (current.uv in 8.0..10.0) {
            binding.uvText.text = "Very High"
        } else if(current.uv > 11.0) {
            binding.uvText.text = "Extreme"
        }

        binding.humidityText.text = current.humidity.toString()
        binding.windText.text = current.windKph.toString() + " km/h"
        binding.feelsikeText.text = "Feels like ${current.feelsLikeCelsius}°"
        val timeOfDay = getTimeOfDay()
        binding.greeting.text = "Good $timeOfDay"
        binding.windDegree.text = "${current.windDegree}°, ${current.windDirection}"
        binding.weatherDescSub.text = "${current.condition.weatherCondition} · ${current.tempCelsius}°"

        if (current.isDay == 1) {
            when(current.condition.weatherCondition) {
                "Sunny" -> Glide.with(this@MainActivity).load(R.drawable.m113).into(binding.weatherImage)
                "Partly Cloudy" -> Glide.with(this@MainActivity).load(R.drawable.m116).into(binding.weatherImage)
                "Cloudy" -> Glide.with(this@MainActivity).load(R.drawable.m119).into(binding.weatherImage)
                "Overcast" -> Glide.with(this@MainActivity).load(R.drawable.m122).into(binding.weatherImage)
                "Mist" -> Glide.with(this@MainActivity).load(R.drawable.m143).into(binding.weatherImage)
                "Patchy rain possible" -> Glide.with(this@MainActivity).load(R.drawable.m176).into(binding.weatherImage)
                "Patchy snow possible" -> Glide.with(this@MainActivity).load(R.drawable.m179).into(binding.weatherImage)
                "Patchy sleet possible" -> Glide.with(this@MainActivity).load(R.drawable.m182).into(binding.weatherImage)
                "Patchy freezing drizzle possible" -> Glide.with(this@MainActivity).load(R.drawable.m185).into(binding.weatherImage)
                "Thundery outbreaks possible" -> Glide.with(this@MainActivity).load(R.drawable.m200).into(binding.weatherImage)
                "Blowing snow" -> Glide.with(this@MainActivity).load(R.drawable.m227).into(binding.weatherImage)
                "Blizzard" -> Glide.with(this@MainActivity).load(R.drawable.m230).into(binding.weatherImage)
                "Fog" -> Glide.with(this@MainActivity).load(R.drawable.m248).into(binding.weatherImage)
                "Freezing fog" -> Glide.with(this@MainActivity).load(R.drawable.m260).into(binding.weatherImage)
                "Patchy light drizzle" -> Glide.with(this@MainActivity).load(R.drawable.m263).into(binding.weatherImage)
                "Light drizzle" -> Glide.with(this@MainActivity).load(R.drawable.m266).into(binding.weatherImage)
                "Freezing drizzle" -> Glide.with(this@MainActivity).load(R.drawable.m281).into(binding.weatherImage)
                "Heavy freezing drizzle" -> Glide.with(this@MainActivity).load(R.drawable.m284).into(binding.weatherImage)
                "Patchy light rain" -> Glide.with(this@MainActivity).load(R.drawable.m293).into(binding.weatherImage)
                "Light rain" -> Glide.with(this@MainActivity).load(R.drawable.m296).into(binding.weatherImage)
                "Moderate rain at times" -> Glide.with(this@MainActivity).load(R.drawable.m299).into(binding.weatherImage)
                "Heavy rain" -> Glide.with(this@MainActivity).load(R.drawable.m308).into(binding.weatherImage)
                "Light freezing rain" -> Glide.with(this@MainActivity).load(R.drawable.m311).into(binding.weatherImage)
                "Moderate or heavy freezing rain" -> Glide.with(this@MainActivity).load(R.drawable.m314).into(binding.weatherImage)
                "Light sleet" -> Glide.with(this@MainActivity).load(R.drawable.m317).into(binding.weatherImage)
                "Moderate or heavy sleet" -> Glide.with(this@MainActivity).load(R.drawable.m320).into(binding.weatherImage)
                "Patchy light snow" -> Glide.with(this@MainActivity).load(R.drawable.m323).into(binding.weatherImage)
                "Light snow" -> Glide.with(this@MainActivity).load(R.drawable.m326).into(binding.weatherImage)
                "Patchy moderate snow" -> Glide.with(this@MainActivity).load(R.drawable.m329).into(binding.weatherImage)
                "Moderate snow" -> Glide.with(this@MainActivity).load(R.drawable.m332).into(binding.weatherImage)
                "Patchy heavy snow" -> Glide.with(this@MainActivity).load(R.drawable.m335).into(binding.weatherImage)
                "Heavy snow" -> Glide.with(this@MainActivity).load(R.drawable.m338).into(binding.weatherImage)
                "Ice pellets" -> Glide.with(this@MainActivity).load(R.drawable.m350).into(binding.weatherImage)
                "Light rain shower" -> Glide.with(this@MainActivity).load(R.drawable.m353).into(binding.weatherImage)
                "Moderate or heavy rain shower" -> Glide.with(this@MainActivity).load(R.drawable.m356).into(binding.weatherImage)
                "Torrential rain shower" -> Glide.with(this@MainActivity).load(R.drawable.m359).into(binding.weatherImage)
                "Light sleet showers" -> Glide.with(this@MainActivity).load(R.drawable.m362).into(binding.weatherImage)
                "Moderate or heavy sleet showers" -> Glide.with(this@MainActivity).load(R.drawable.m365).into(binding.weatherImage)
                "Light snow showers" -> Glide.with(this@MainActivity).load(R.drawable.m368).into(binding.weatherImage)
                "Moderate or heavy snow showers" -> Glide.with(this@MainActivity).load(R.drawable.m371).into(binding.weatherImage)
                "Light showers of ice pellets" -> Glide.with(this@MainActivity).load(R.drawable.m374).into(binding.weatherImage)
                "Moderate or heavy showers of ice pellets" -> Glide.with(this@MainActivity).load(R.drawable.m377).into(binding.weatherImage)
                "Patchy light rain with thunder" -> Glide.with(this@MainActivity).load(R.drawable.m386).into(binding.weatherImage)
                "Moderate or heavy rain with thunder" -> Glide.with(this@MainActivity).load(R.drawable.m389).into(binding.weatherImage)
                "Patchy light snow with thunder" -> Glide.with(this@MainActivity).load(R.drawable.m392).into(binding.weatherImage)
                "Moderate or heavy snow with thunder" -> Glide.with(this@MainActivity).load(R.drawable.m395).into(binding.weatherImage)

                else -> {
                    Log.d("API Error", "Invalid weather condition. ${current.condition.weatherCondition}")
                }

            }
        } else if (current.isDay == 0) {
            when(current.condition.weatherCondition) {
                "Clear" -> Glide.with(this@MainActivity).load(R.drawable.n113).into(binding.weatherImage)
                "Partly cloudy" -> Glide.with(this@MainActivity).load(R.drawable.n116).into(binding.weatherImage)
                "Cloudy" -> Glide.with(this@MainActivity).load(R.drawable.n119).into(binding.weatherImage)
                "Overcast" -> Glide.with(this@MainActivity).load(R.drawable.n122).into(binding.weatherImage)
                "Mist" -> Glide.with(this@MainActivity).load(R.drawable.n143).into(binding.weatherImage)
                "Patchy rain possible" -> Glide.with(this@MainActivity).load(R.drawable.n176).into(binding.weatherImage)
                "Patchy snow possible" -> Glide.with(this@MainActivity).load(R.drawable.n179).into(binding.weatherImage)
                "Patchy sleet possible" -> Glide.with(this@MainActivity).load(R.drawable.n182).into(binding.weatherImage)
                "Patchy freezing drizzle possible" -> Glide.with(this@MainActivity).load(R.drawable.n185).into(binding.weatherImage)
                "Thundery outbreaks possible" -> Glide.with(this@MainActivity).load(R.drawable.n200).into(binding.weatherImage)
                "Blowing snow" -> Glide.with(this@MainActivity).load(R.drawable.n227).into(binding.weatherImage)
                "Blizzard" -> Glide.with(this@MainActivity).load(R.drawable.n230).into(binding.weatherImage)
                "Fog" -> Glide.with(this@MainActivity).load(R.drawable.n248).into(binding.weatherImage)
                "Freezing fog" -> Glide.with(this@MainActivity).load(R.drawable.n260).into(binding.weatherImage)
                "Patchy light drizzle" -> Glide.with(this@MainActivity).load(R.drawable.n263).into(binding.weatherImage)
                "Light drizzle" -> Glide.with(this@MainActivity).load(R.drawable.n266).into(binding.weatherImage)
                "Freezing drizzle" -> Glide.with(this@MainActivity).load(R.drawable.n281).into(binding.weatherImage)
                "Heavy freezing drizzle" -> Glide.with(this@MainActivity).load(R.drawable.n284).into(binding.weatherImage)
                "Patchy light rain" -> Glide.with(this@MainActivity).load(R.drawable.n293).into(binding.weatherImage)
                "Light rain" -> Glide.with(this@MainActivity).load(R.drawable.n296).into(binding.weatherImage)
                "Moderate rain at times" -> Glide.with(this@MainActivity).load(R.drawable.n299).into(binding.weatherImage)
                "Heavy rain" -> Glide.with(this@MainActivity).load(R.drawable.n308).into(binding.weatherImage)
                "Light freezing rain" -> Glide.with(this@MainActivity).load(R.drawable.n311).into(binding.weatherImage)
                "Moderate or heavy freezing rain" -> Glide.with(this@MainActivity).load(R.drawable.n314).into(binding.weatherImage)
                "Light sleet" -> Glide.with(this@MainActivity).load(R.drawable.n317).into(binding.weatherImage)
                "Moderate or heavy sleet" -> Glide.with(this@MainActivity).load(R.drawable.n320).into(binding.weatherImage)
                "Patchy light snow" -> Glide.with(this@MainActivity).load(R.drawable.n323).into(binding.weatherImage)
                "Light snow" -> Glide.with(this@MainActivity).load(R.drawable.n326).into(binding.weatherImage)
                "Patchy moderate snow" -> Glide.with(this@MainActivity).load(R.drawable.n329).into(binding.weatherImage)
                "Moderate snow" -> Glide.with(this@MainActivity).load(R.drawable.n332).into(binding.weatherImage)
                "Patchy heavy snow" -> Glide.with(this@MainActivity).load(R.drawable.n335).into(binding.weatherImage)
                "Heavy snow" -> Glide.with(this@MainActivity).load(R.drawable.n338).into(binding.weatherImage)
                "Ice pellets" -> Glide.with(this@MainActivity).load(R.drawable.n350).into(binding.weatherImage)
                "Light rain shower" -> Glide.with(this@MainActivity).load(R.drawable.n353).into(binding.weatherImage)
                "Moderate or heavy rain shower" -> Glide.with(this@MainActivity).load(R.drawable.n356).into(binding.weatherImage)
                "Torrential rain shower" -> Glide.with(this@MainActivity).load(R.drawable.n359).into(binding.weatherImage)
                "Light sleet showers" -> Glide.with(this@MainActivity).load(R.drawable.n362).into(binding.weatherImage)
                "Moderate or heavy sleet showers" -> Glide.with(this@MainActivity).load(R.drawable.n365).into(binding.weatherImage)
                "Light snow showers" -> Glide.with(this@MainActivity).load(R.drawable.n368).into(binding.weatherImage)
                "Moderate or heavy snow showers" -> Glide.with(this@MainActivity).load(R.drawable.n371).into(binding.weatherImage)
                "Light showers of ice pellets" -> Glide.with(this@MainActivity).load(R.drawable.n374).into(binding.weatherImage)
                "Moderate or heavy showers of ice pellets" -> Glide.with(this@MainActivity).load(R.drawable.n377).into(binding.weatherImage)
                "Patchy light rain with thunder" -> Glide.with(this@MainActivity).load(R.drawable.n386).into(binding.weatherImage)
                "Moderate or heavy rain with thunder" -> Glide.with(this@MainActivity).load(R.drawable.n389).into(binding.weatherImage)
                "Patchy light snow with thunder" -> Glide.with(this@MainActivity).load(R.drawable.n392).into(binding.weatherImage)
                "Moderate or heavy snow with thunder" -> Glide.with(this@MainActivity).load(R.drawable.n395).into(binding.weatherImage)
                else -> {
                    Log.d("API Error", "Invalid weather condition. ${current.condition.weatherCondition}")
                }
            }

        }
    }
    private fun AstronomyData.bind() {
        binding.sunriseText.text = astronomy.astro.sunrise
        binding.sunsetText.text = astronomy.astro.sunset

        binding.swiperefresh.isRefreshing = false
    }

    private fun getCurrentWeatherData() {
        val responseLiveData = viewModel.getCurrentLocationWeather()

        responseLiveData.observe(this) { response ->
            if (response.isSuccessful) {
                val currentWeather = response.body()
                currentWeather?.bind()
            } else {
                Toast.makeText(this@MainActivity, "Error getting current weather data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getAstronomyData() {
        val responseLiveData = viewModel.getAstronomyData()

        responseLiveData.observe(this) { response ->
            if (response.isSuccessful) {
                val astronomy = response.body()
                    astronomy?.bind()
                    Log.d("astro", response.body().toString())
            } else {
                Toast.makeText(this@MainActivity, "Error getting Astronomy data", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun convertDate(inputDate: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val outputFormat = SimpleDateFormat("EEE, HH:mm", Locale.getDefault())

            val date: Date? = inputFormat.parse(inputDate)
            return outputFormat.format(date!!)

    }

    private fun getTimeOfDay(): String {
        val calendar = Calendar.getInstance()

        return when (calendar.get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "morning"
            in 12..16 -> "afternoon"
            in 17..23 -> "evening"
            else -> "Unknown"
        }
    }



}