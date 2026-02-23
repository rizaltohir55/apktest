package com.yourapp.pricetracker.domain.repository

import com.yourapp.pricetracker.domain.model.Asset

interface IWatchlistRepository {
    suspend fun getWatchlist(): List<Asset>
}
