package com.yourapp.pricetracker.presentation.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourapp.pricetracker.domain.model.Asset
import com.yourapp.pricetracker.domain.model.AssetCategory
import com.yourapp.pricetracker.domain.model.Price
import com.yourapp.pricetracker.domain.repository.IPriceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val priceRepository: IPriceRepository
) : ViewModel() {

    private val _assets = MutableStateFlow<List<Pair<Asset, Price>>>(emptyList())
    val assets: StateFlow<List<Pair<Asset, Price>>> = _assets.asStateFlow()

    init {
        viewModelScope.launch {
            val initialAssets = listOf(
                Asset("BTCUSDT", "Bitcoin", AssetCategory.CRYPTO) to Price("BTCUSDT", 62000.0, 150.0, 0.24, System.currentTimeMillis()),
                Asset("ETHUSDT", "Ethereum", AssetCategory.CRYPTO) to Price("ETHUSDT", 3400.0, -20.0, -0.5, System.currentTimeMillis()),
                Asset("XAU/USD", "Gold", AssetCategory.COMMODITY) to Price("XAU/USD", 2350.0, 5.0, 0.2, System.currentTimeMillis()),
                Asset("EUR/USD", "Euro", AssetCategory.FOREX) to Price("EUR/USD", 1.08, 0.005, 0.46, System.currentTimeMillis())
            )
            _assets.value = initialAssets

            while(kotlinx.coroutines.isActive) {
                kotlinx.coroutines.delay(5000)
                val updatedAssets = _assets.value.map { (asset, price) ->
                    if (asset.category == AssetCategory.CRYPTO) {
                        try {
                            val newPrice = priceRepository.fetchLatestPrice(asset.symbol)
                            val change = newPrice.current - price.current
                            val changePercent = if (price.current > 0) (change / price.current) * 100 else 0.0
                            asset to newPrice.copy(change = change, changePercent = changePercent)
                        } catch(e: Exception) {
                            asset to price
                        }
                    } else {
                        val randomChange = (Math.random() - 0.5) * (price.current * 0.001)
                        val newCurrent = price.current + randomChange
                        val newChange = price.change + randomChange
                        val newChangePercent = (newChange / (newCurrent - newChange)) * 100
                        asset to price.copy(
                            current = newCurrent,
                            change = newChange,
                            changePercent = newChangePercent,
                            timestamp = System.currentTimeMillis()
                        )
                    }
                }
                _assets.value = updatedAssets
            }
        }
    }
}
