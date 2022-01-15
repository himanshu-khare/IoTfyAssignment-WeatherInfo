package com.iotfyassignment.data.repository

import android.os.Build
import com.iotfyassignment.BuildConfig
import com.iotfyassignment.data.model.WeatherResponse
import com.iotfyassignment.networking.CheckResponse
import com.iotfyassignment.networking.OpenWeatherApi
import com.iotfyassignment.networking.Resource
import com.iotfyassignment.networking.getCause
import javax.inject.Inject

interface WeatherRepository{
    suspend fun getWeather(city: String):Resource<WeatherResponse>
}

class DefaultWeatherRepository @Inject constructor(
    private val api: OpenWeatherApi
): WeatherRepository{
    override suspend fun getWeather(city: String): Resource<WeatherResponse> {
       return try {
           CheckResponse(api.getWeather(city, BuildConfig.OPW_KEY,"metric")).handleResponse()
       }catch (e:Exception){
           Resource.Error(null,e.getCause())
       }
    }
}