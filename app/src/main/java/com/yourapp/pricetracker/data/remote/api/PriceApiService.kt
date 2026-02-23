package com.yourapp.pricetracker.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Query

import com.yourapp.pricetracker.data.remote.dto.BinancePriceDto
import com.yourapp.pricetracker.data.remote.dto.TwelveDataPriceDto
import com.yourapp.pricetracker.data.remote.dto.TwelveDataQuoteDto

interface PriceApiService {
    @GET("ticker/price")
    suspend fun getPrice(@Query("symbol") symbol: String): BinancePriceDto

    @GET("price")
    suspend fun getTwelveDataPrice(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = com.yourapp.pricetracker.BuildConfig.TWELVE_DATA_KEY
    ): TwelveDataPriceDto

    @GET("quote")
    suspend fun getTwelveDataQuote(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = com.yourapp.pricetracker.BuildConfig.TWELVE_DATA_KEY
    ): TwelveDataQuoteDto
}
