package com.example.weather

import android.content.ContentValues.TAG
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.weather.databinding.ActivityMainBinding
import okhttp3.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import kotlin.math.log

//c548c7a27ab4bada6b6db3b572b8a4ed
class MainActivity : AppCompatActivity() {
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchWeatherData()
    }

    private fun fetchWeatherData() {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        val response = retrofit.getWeatherData(city = "tunis", appid ="c548c7a27ab4bada6b6db3b572b8a4ed" , units = "metric" )
        response.enqueue(object : Callback<WeatherApp>{
            override fun onResponse(call: retrofit2.Call<WeatherApp>, response: Response<WeatherApp>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    val temperature = responseBody.main.temp.toString()
                    binding.Tem
                    //Log.d("TAG", "onResponse: $temperature")
                }
            }

            override fun onFailure(call: retrofit2.Call<WeatherApp>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }
}