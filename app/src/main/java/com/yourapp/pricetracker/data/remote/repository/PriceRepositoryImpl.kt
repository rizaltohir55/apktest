package com.yourapp.pricetracker.data.remote.repository

import com.yourapp.pricetracker.data.local.database.dao.PriceDao
import com.yourapp.pricetracker.data.local.database.dao.WatchlistDao
import com.yourapp.pricetracker.data.remote.api.PriceApiService
import com.yourapp.pricetracker.data.remote.api.WebSocketService
import com.yourapp.pricetracker.domain.model.Price
import com.yourapp.pricetracker.domain.repository.IPriceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class PriceRepositoryImpl @Inject constructor(
    @Named("binance") private val binanceApi: PriceApiService,
    @Named("twelveData") private val twelveDataApi: PriceApiService,
    private val webSocketService: WebSocketService,
    private val priceDao: PriceDao,
    private val watchlistDao: WatchlistDao
) : IPriceRepository {
    override fun getRealtimePrice(symbol: String): Flow<Price> {
        val mappedSymbol = symbol.replace("/", "").uppercase()
        // If it's a forex or non-crypto, we might fallback to REST polling elsewhere,
        // but for now we emit from the websocket flow which processes Binance cryptos
        return webSocketService.priceFlow
            .kotlinx.coroutines.flow.filter { it.symbol.equals(mappedSymbol, ignoreCase = true) }
            .map { ticker ->
                val current = ticker.currentPrice.toDoubleOrNull() ?: 0.0
                val change = ticker.priceChange.toDoubleOrNull() ?: 0.0
                val changePercent = ticker.priceChangePercent.toDoubleOrNull() ?: 0.0
                Price(symbol, current, change, changePercent, ticker.eventTime)
            }
    }

    override suspend fun fetchLatestPrice(symbol: String): Price {
        val isCrypto = symbol.contains("USDT") || symbol.contains("BTC") || symbol.contains("ETH")
        return try {
            if (isCrypto) {
                val mappedSymbol = symbol.replace("/", "").uppercase()
                val response = binanceApi.getPrice(mappedSymbol)
                val currentPrice = response.price.toDoubleOrNull() ?: 0.0
                Price(symbol, currentPrice, 0.0, 0.0, System.currentTimeMillis())
            } else {
                val mappedSymbol = symbol.uppercase()
                val response = twelveDataApi.getTwelveDataPrice(mappedSymbol)
                val currentPrice = response.price.toDoubleOrNull() ?: 0.0
                Price(symbol, currentPrice, 0.0, 0.0, System.currentTimeMillis())
            }
        } catch (e: Exception) {
            Price(symbol, 0.0, 0.0, 0.0, System.currentTimeMillis()) // fallback
        }
    }

    override suspend fun savePrice(price: Price) {
        // Placeholder
    }

    override suspend fun getWatchlist(): List<String> {
        return watchlistDao.getAllWatchlist().map { it.symbol }
    }
}
