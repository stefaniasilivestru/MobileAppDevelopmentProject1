package com.example.project1.ui.weather

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.project1.PlaceAdapter
import com.example.project1.R
import com.example.project1.databinding.FragmentWeatherBinding
import com.example.project1.ui.weather.models.CurrentWeather
import com.example.project1.ui.weather.utils.RetrofitInstance
import com.example.project1.ui.weather.utils.Util
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentWeatherBinding.inflate(inflater, container, false)

        getCurrentWeather()
        return binding.root
    }

    private fun getCurrentWeather() {
        GlobalScope.launch(Dispatchers.IO) {
           val response = try {
               val sharedPreferences = context?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
               val routeName = sharedPreferences?.getString("routeName", null)
               if (routeName != null) {
                   RetrofitInstance.api.getCurrentWeather(routeName, "metric", Util.API_KEY)
               } else {
                     withContext(Dispatchers.Main) {
                          Toast.makeText(context, "Route name is null", Toast.LENGTH_SHORT).show()
                     }
                     return@launch
               }

           } catch (e: IOException) {
               Toast.makeText(context, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
               return@launch
           } catch (e: HttpException) {
               Toast.makeText(context, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
                return@launch
           }

            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    fillUIWithData(response)
                }
            }
        }
    }

    private fun fillUIWithData(response: Response<CurrentWeather>) {
        val data = response.body()
        binding.locationTextView.text = data?.name

        // get the image of the weather
        val iconId = data?.weather?.get(0)?.icon
        Log.d("WeatherFragment", "Icon id: $iconId")
        val imgUrl = "http://openweathermap.org/img/w/$iconId.png"
        Log.d("WeatherFragment", "Image url: $imgUrl")

        Picasso.get()
            .load(imgUrl)
            .into(binding.weatherIconImageView, object : Callback {
                override fun onSuccess() {
                    Log.d("Picasso", "Image loaded successfully")
                }

                override fun onError(e: Exception?) {
                    Log.e("Picasso", "Error loading image: ${e?.message}")
                }
            })

        binding.apply {
            lastUpdateTextView.text = "Last update: ${
                SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(
                    data?.dt?.times(1000) ?: 0
                )
            }"
            temperatureTextView.text = "${data?.main?.temp}°C"
            descriptionTextView.text = data?.weather?.get(0)?.main
            temperatureTextView.text = "${data?.main?.temp?.toInt()}°C"
            feelsLikeTextView.text = "Feels like: ${data?.main?.feelsLike?.toInt()}°C"
            minTemperatureTextView.text = "Min: ${data?.main?.tempMin?.toInt()}°C"
            maxTemperatureTextView.text = "Max: ${data?.main?.tempMax?.toInt()}°C"

        }
    }
}

