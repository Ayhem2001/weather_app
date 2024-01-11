package com.example.weather

import android.content.ContentValues.TAG
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import com.example.weather.databinding.ActivityMainBinding
import com.google.android.material.color.utilities.ViewingConditions
import okhttp3.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.log

//c548c7a27ab4bada6b6db3b572b8a4ed
class MainActivity : AppCompatActivity() {
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchWeatherData("tunis")
        SearchCity()
    }

    private fun SearchCity() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true}

            override fun onQueryTextChange(newText: String?): Boolean {
                return true            }

        })}

    private fun fetchWeatherData(cityName:String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        val response = retrofit.getWeatherData(cityName, appid ="c548c7a27ab4bada6b6db3b572b8a4ed" , units = "metric" )
        response.enqueue(object : Callback<WeatherApp>{
            override fun onResponse(call: retrofit2.Call<WeatherApp>, response: Response<WeatherApp>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    val temperature = responseBody.main.temp.toString()
                    val humidity = responseBody.main.humidity
                    val windSpeed = responseBody.wind.speed
                    val sunRise = responseBody.sys.sunrise.toLong()
                    val sunSet = responseBody.sys.sunset.toLong()
                    val sealevel = responseBody.main.pressure
                    val condition = responseBody.weather.firstOrNull()?.main?: "unknown"
                    val maxTemp = responseBody.main.temp_max
                    val minTemp = responseBody.main.temp_min
                    binding.Tem.text="$temperature °C"
                      binding.Weather.text = condition
                      binding.MaxTem.text = "Max Temp : $maxTemp °C"
                      binding.MinTem.text = "Max Temp : $minTemp °C"
                      binding.Humidity.text = "$humidity %"
                      binding.WindSpeed.text = "$windSpeed m/s"
                      binding.SunRise.text = "${time(sunRise)}"
                      binding.Sunset.text = "${time(sunSet)}"
                      binding.Sea.text = "$sealevel hPa"
                      binding.Condition.text = condition
                      binding.Day.text=dayName(System.currentTimeMillis())
                          binding.Date.text=date()
                          binding.CityName.text="$cityName"
                    //Log.d("TAG", "onResponse: $temperature")

                    changeImagesaccordingToWeatherCondition(condition)
                }
            }

            override fun onFailure(call: retrofit2.Call<WeatherApp>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun changeImagesaccordingToWeatherCondition(conditions: String) {
        when(conditions){
            "Clear Sky" , "Sunny", "Clear" ->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
            "Partly Clouds" , "Clouds", "Overcast" , "Mist" , "Foggy" ->{
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }
            "Light Rain" , "Drizzle", "Moderate Rain" , "Showers" , "Heavy Rain" ->{
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }
            "Light Snow" , "Moderate Snow", "Heavy Snow" , "Blizzard" ->{
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }
            else ->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
        }
        binding.lottieAnimationView.playAnimation()
    }

    fun dayName(timestamp: Long): String{
        val sdf = SimpleDateFormat("EEEE" , Locale.getDefault())
        return sdf.format((Date()))
    }
    private fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM YYYY" , Locale.getDefault())
        return sdf.format((Date()))
    }
    private fun time(timestamp: Long): String{
        val sdf = SimpleDateFormat("HH:mm" , Locale.getDefault())
        return sdf.format((Date(timestamp*1000)))
    }


}