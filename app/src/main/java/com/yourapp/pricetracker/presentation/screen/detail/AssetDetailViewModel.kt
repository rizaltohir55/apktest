package com.yourapp.pricetracker.presentation.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourapp.pricetracker.domain.model.*
import com.yourapp.pricetracker.domain.repository.IPriceRepository
import com.yourapp.pricetracker.ml.indicator.BollingerBandsCalculator
import com.yourapp.pricetracker.ml.indicator.EMACalculator
import com.yourapp.pricetracker.ml.indicator.MACDCalculator
import com.yourapp.pricetracker.ml.indicator.RSICalculator
import com.yourapp.pricetracker.ml.model.PricePredictionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AssetDetailState(
    val symbol: String = "",
    val currentPrice: Price? = null,
    val candles: List<Candle> = emptyList(),
    val indicators: TechnicalIndicators = TechnicalIndicators.empty(),
    val prediction: PredictionResult? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class AssetDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val priceRepository: IPriceRepository,
    private val predictionModel: PricePredictionModel
) : ViewModel() {

    private val symbol: String = savedStateHandle.get<String>("symbol") ?: ""
    private val decodedSymbol = java.net.URLDecoder.decode(symbol, "UTF-8")

    private val _state = MutableStateFlow(AssetDetailState(symbol = decodedSymbol))
    val state = _state.asStateFlow()

    init {
        predictionModel.initialize()
        loadInitialData()
        observeRealtimePrices()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            // Simulate loading historical candles (60 periods)
            val mockCandles = mutableListOf<Candle>()
            var basePrice = 50000.0 // Mock base
            val now = System.currentTimeMillis()
            
            for (i in 60 downTo 1) {
                basePrice += (Math.random() - 0.5) * 100
                mockCandles.add(
                    Candle(
                        timestamp = now - (i * 60000), // 1m candles
                        open = basePrice - 10,
                        high = basePrice + 20,
                        low = basePrice - 20,
                        close = basePrice,
                        volume = 100.0 * Math.random()
                    )
                )
            }
            
            val livePrice = priceRepository.fetchLatestPrice(decodedSymbol)
            updateStateWithNewData(mockCandles, livePrice)
        }
    }

    private fun observeRealtimePrices() {
        viewModelScope.launch {
            priceRepository.getRealtimePrice(decodedSymbol).collect { livePrice ->
                // In a real app we'd update/append the last candle here
                // For simplicity we just run prediction recalculation
                val currentCandles = _state.value.candles
                if (currentCandles.isNotEmpty()) {
                    val updatedCandles = currentCandles.toMutableList()
                    val last = updatedCandles.last()
                    // Update current close specifically
                    updatedCandles[updatedCandles.lastIndex] = last.copy(
                        close = livePrice.current,
                        high = maxOf(last.high, livePrice.current),
                        low = minOf(last.low, livePrice.current)
                    )
                    updateStateWithNewData(updatedCandles, livePrice)
                }
            }
        }
    }

    private fun updateStateWithNewData(candles: List<Candle>, price: Price) {
        val closePrices = candles.map { it.close }
        
        val rsiValues = RSICalculator.calculate(closePrices)
        val macdValues = MACDCalculator.calculate(closePrices)
        val bbValues = BollingerBandsCalculator.calculate(closePrices)
        val ema20Values = EMACalculator.calculate(closePrices, 20)
        val ema50Values = EMACalculator.calculate(closePrices, 50)
        
        val currentIndicators = TechnicalIndicators(
            rsi = rsiValues.lastOrNull(),
            macd = macdValues.lastOrNull(),
            bollingerBands = bbValues.lastOrNull(),
            ema20 = ema20Values.lastOrNull(),
            ema50 = ema50Values.lastOrNull()
        )
        
        val prediction = predictionModel.predict(candles, currentIndicators)
        
        _state.value = _state.value.copy(
            currentPrice = price,
            candles = candles,
            indicators = currentIndicators,
            prediction = prediction,
            isLoading = false
        )
    }
}
