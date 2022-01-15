package com.iotfyassignment.di

import android.content.Context
import com.iotfyassignment.BuildConfig
import com.iotfyassignment.data.repository.DefaultWeatherRepository
import com.iotfyassignment.data.repository.WeatherRepository
import com.iotfyassignment.networking.OpenWeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule{
    @Singleton
    @Provides
    fun providesFitooseApis(): OpenWeatherApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.OPW_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenWeatherApi::class.java)
    }

    @Singleton
    @Provides
    fun providesWeatherRepository(apis: OpenWeatherApi): WeatherRepository =
        DefaultWeatherRepository(apis)
}