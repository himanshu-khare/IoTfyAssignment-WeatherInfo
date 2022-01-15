package com.iotfyassignment.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.iotfyassignment.data.model.WeatherResponse
import com.iotfyassignment.data.repository.DefaultWeatherRepository
import com.iotfyassignment.networking.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherInfoViewModel @Inject  constructor(
    private val repository: DefaultWeatherRepository
) : ViewModel() {

    private var latlong = LatLng(28.6139, 77.2090)

    private val _weatherInfo = MutableStateFlow<Resource<WeatherResponse>>(Resource.Empty())
    val weatherInfo = _weatherInfo.asStateFlow()

    init {
        getWeatherInfo("New Delhi")
    }

    fun getLatLong(): LatLng {
        return latlong
    }

    fun setLatLong(latitude: Double, longitude: Double) {
        latlong = LatLng(latitude, longitude)
    }


    fun getWeatherInfo(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _weatherInfo.value = Resource.Loading()
            _weatherInfo.value = repository.getWeather(city)
        }
    }

}