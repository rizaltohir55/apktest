package com.yourapp.pricetracker.core.di

import com.yourapp.pricetracker.data.remote.api.PriceApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp.MediaType.Companion.toMediaType
import okhttp.OkHttpClient
import okhttp.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    @Named("binance")
    fun provideBinanceApi(
        okHttpClient: OkHttpClient,
        json: Json
    ): PriceApiService = Retrofit.Builder()
        .baseUrl("https://api.binance.com/api/v3/")
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(PriceApiService::class.java)

    @Provides
    @Singleton
    @Named("alphaVantage")
    fun provideAlphaVantageApi(
        okHttpClient: OkHttpClient,
        json: Json
    ): PriceApiService = Retrofit.Builder()
        .baseUrl("https://www.alphavantage.co/")
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(PriceApiService::class.java)

    @Provides
    @Singleton
    @Named("coingecko")
    fun provideCoinGeckoApi(
        okHttpClient: OkHttpClient,
        json: Json
    ): PriceApiService = Retrofit.Builder()
        .baseUrl("https://api.coingecko.com/api/v3/")
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(PriceApiService::class.java)
    
    @Provides
    @Singleton
    @Named("twelveData")
    fun provideTwelveDataApi(
        okHttpClient: OkHttpClient,
        json: Json
    ): PriceApiService = Retrofit.Builder()
        .baseUrl("https://api.twelvedata.com/")
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(PriceApiService::class.java)

    @Provides
    @Singleton
    fun provideWebSocketService(
        okHttpClient: OkHttpClient,
        json: Json
    ): com.yourapp.pricetracker.data.remote.api.WebSocketService {
        return com.yourapp.pricetracker.data.remote.api.WebSocketService(okHttpClient, json)
    }
}
