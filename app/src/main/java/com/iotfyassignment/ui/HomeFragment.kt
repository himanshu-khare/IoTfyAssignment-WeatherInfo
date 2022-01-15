package com.iotfyassignment.ui

import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.app.akplacepicker.models.AddressData
import com.app.akplacepicker.utilities.Constants
import com.app.akplacepicker.utilities.PlacePicker
import com.iotfyassignment.BuildConfig
import com.iotfyassignment.R
import com.iotfyassignment.data.model.WeatherResponse
import com.iotfyassignment.data.viewmodel.WeatherInfoViewModel
import com.iotfyassignment.databinding.FragmentHomeBinding
import com.iotfyassignment.networking.Resource
import com.iotfyassignment.utils.WeatherConditions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment((R.layout.fragment_home)) {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: WeatherInfoViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        binding.selectCityBtn.setOnClickListener {
            val intent = selectLocation()
            resultLauncherAddress.launch(intent)
        }
        collectWeatherInfo()
    }

    private val resultLauncherAddress =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val addressData: AddressData = data?.getParcelableExtra(Constants.ADDRESS_INTENT)!!
                val geocoder = Geocoder(activity, Locale.getDefault())
                viewModel.setLatLong(addressData.latitude, addressData.longitude)
                try {
                    val addresses = geocoder.getFromLocation(addressData.latitude, addressData.longitude, 1)
                    val city = addresses.first().subAdminArea
                    viewModel.getWeatherInfo(city)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }


    private fun selectLocation(): Intent {
        val placeBuilder = PlacePicker.IntentBuilder()
        placeBuilder.setGoogleMapApiKey(BuildConfig.GMP_KEY)
        placeBuilder.setLatLong(
            viewModel.getLatLong().latitude,
            viewModel.getLatLong().longitude
        )
        placeBuilder.setMapZoom(20.0f)
        placeBuilder.setAddressRequired(false)
        placeBuilder.setPrimaryTextColor(R.color.black)
        return placeBuilder.build(activity)
    }

    private fun collectWeatherInfo() {
        lifecycleScope.launch {
            viewModel.weatherInfo.collect {
                when (it) {
                    is Resource.Empty -> {}
                    is Resource.Error -> {
                        binding.loadingImage.visibility = View.GONE
                        binding.weatherInfoDescription.text = it.message
                        binding.lottieAnimationView.setAnimation(R.raw.notfound)
                        binding.lottieAnimationView.playAnimation()
                    }
                    is Resource.Loading -> {
                        binding.loadingImage.visibility = View.VISIBLE
                        binding.weatherInfoDescription.text = "..."
                    }
                    is Resource.Success -> {
                        binding.loadingImage.visibility = View.GONE
                        it.data?.apply {
                            setWeatherInfo(this)
                            setAnimation(weather?.first()?.main)
                        }
                    }
                }
            }
        }
    }

    private fun setWeatherInfo(weatherResponse: WeatherResponse) {
        weatherResponse.apply {
            val description = weather?.first()?.description
            val temp = main?.temp
            val tempMax = main?.temp_max
            val tempMin = main?.temp_min
            val city = name
            val totalDescription = "$city:- \n" +
                    "${"Weather: " + description?.uppercase()} \n" +
                    "${"Temp: $temp℃"} \n" +
                    "${"MaxTemp: $tempMax℃"} \n" +
                    "MinTemp: $tempMin℃"
            binding.weatherInfoDescription.text = totalDescription
        }
    }

    private fun setAnimation(icon: String?) {
        binding.lottieAnimationView.apply {
            when (icon) {
                WeatherConditions.Clear.name -> {
                    setAnimation(R.raw.weather_sunny)
                }
                WeatherConditions.Clouds.name -> {
                   setAnimation(R.raw.weather_cloudy)
                }
                WeatherConditions.Drizzle.name -> {
                    setAnimation(R.raw.weather_icon_drizzle)
                }
                WeatherConditions.Rain.name -> {
                   setAnimation(R.raw.weather_partly_shower)
                }
                WeatherConditions.Snow.name -> {
                   setAnimation(R.raw.weather_heavy_snow)
                }
                WeatherConditions.Thunderstorm.name -> {
                   setAnimation(R.raw.weather_thunderstorm)
                }
                else -> {
                   setAnimation(R.raw.weather_atmosphere)
                }
            }
            playAnimation()
        }
    }
}