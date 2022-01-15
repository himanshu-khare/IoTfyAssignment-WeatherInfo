package com.iotfyassignment.networking

import com.iotfyassignment.data.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {
    @GET("data/2.5/weather")
    suspend fun getWeather(@Query("q") city: String,@Query("appId") appId:String,@Query("units") units:String): Response<WeatherResponse>
}
