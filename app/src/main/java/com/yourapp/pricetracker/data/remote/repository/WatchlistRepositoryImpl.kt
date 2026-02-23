package com.yourapp.pricetracker.data.remote.repository

import com.yourapp.pricetracker.data.local.database.dao.WatchlistDao
import com.yourapp.pricetracker.domain.model.Asset
import com.yourapp.pricetracker.domain.repository.IWatchlistRepository
import javax.inject.Inject

class WatchlistRepositoryImpl @Inject constructor(
    private val watchlistDao: WatchlistDao
) : IWatchlistRepository {
    override suspend fun getWatchlist(): List<Asset> {
        return watchlistDao.getAllWatchlist().map { Asset(it.symbol, it.name, it.category) }
    }
}
