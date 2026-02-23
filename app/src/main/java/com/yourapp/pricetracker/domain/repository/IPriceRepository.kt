package com.yourapp.pricetracker.domain.repository

import com.yourapp.pricetracker.domain.model.Price
import kotlinx.coroutines.flow.Flow

interface IPriceRepository {
    fun getRealtimePrice(symbol: String): Flow<Price>
    suspend fun fetchLatestPrice(symbol: String): Price
    suspend fun savePrice(price: Price)
    suspend fun getWatchlist(): List<String>
}
