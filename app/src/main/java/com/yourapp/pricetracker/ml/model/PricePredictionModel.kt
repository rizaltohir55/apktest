package com.yourapp.pricetracker.ml.model

import android.content.Context
import com.yourapp.pricetracker.domain.model.Candle
import com.yourapp.pricetracker.domain.model.PredictionResult
import com.yourapp.pricetracker.domain.model.Signal
import com.yourapp.pricetracker.domain.model.TechnicalIndicators
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.math.max

/**
 * Simulated Model replacing actual TensorFlow Lite interpreter
 * Executes a rule-based approach over the exact same input buffers intended for the Neural Net
 */
class PricePredictionModel @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val INPUT_SIZE = 60    // 60 candle lookback
    private val OUTPUT_SIZE = 5    // 5 periode ke depan

    fun initialize() {
        // In a real scenario, we load the .tflite from assets here.
        // val modelFile = loadModelFile("price_prediction.tflite")
        // val options = Interpreter.Options().apply { addDelegate(GpuDelegate()) }
        // interpreter = Interpreter(modelFile, options)
    }

    fun predict(candles: List<Candle>, indicators: TechnicalIndicators): PredictionResult {
        if (candles.size < INPUT_SIZE) {
            val lastClose = candles.lastOrNull()?.close ?: 0.0
            return PredictionResult(List(OUTPUT_SIZE) { lastClose }, Signal.HOLD, 0.0f)
        }

        val currentPrice = candles.last().close
        
        // 1. Calculate Confidence based on Volatility (ATR-like approach)
        val trueRanges = candles.windowed(2).map { 
            val prev = it[0]
            val curr = it[1]
            max(curr.high - curr.low, max(Math.abs(curr.high - prev.close), Math.abs(curr.low - prev.close)))
        }
        val atr = trueRanges.takeLast(14).average()
        
        // Less volatility = higher confidence in the trend
        val volatilityRatio = (atr / currentPrice).coerceIn(0.0, 0.05)
        val baseConfidence = (1.0 - (volatilityRatio / 0.05)).toFloat().coerceIn(0.4f, 0.95f)

        // 2. Determine Signal from Indicators
        var score = 0
        var confidenceBoost = 0f

        // RSI Logic
        val rsi = indicators.rsi ?: 50.0
        if (rsi < 30.0) { score += 1; confidenceBoost += 0.05f } // Oversold -> BUY
        else if (rsi > 70.0) { score -= 1; confidenceBoost += 0.05f } // Overbought -> SELL

        // MACD Logic
        val macd = indicators.macd
        if (macd != null) {
            if (macd.histogram > 0 && macd.macdLine > macd.signalLine) {
                score += 1
                confidenceBoost += 0.05f
            } else if (macd.histogram < 0 && macd.macdLine < macd.signalLine) {
                score -= 1
                confidenceBoost += 0.05f
            }
        }

        // Bollinger Bands Logic
        val bb = indicators.bollingerBands
        if (bb != null) {
            if (currentPrice < bb.lowerBand) score += 1 // Bounce up expected
            else if (currentPrice > bb.upperBand) score -= 1 // Pullback expected
        }
        
        val finalSignal = when {
            score >= 2 -> Signal.BUY
            score <= -2 -> Signal.SELL
            else -> Signal.HOLD
        }

        val finalConfidence = (baseConfidence + confidenceBoost).coerceIn(0.0f, 0.99f)

        // 3. Synthesize Output Tensors (Future prices)
        val trendMultiplier = when(finalSignal) {
            Signal.BUY -> 1.002
            Signal.SELL -> 0.998
            Signal.HOLD -> 1.0
        }
        
        val predictedPrices = mutableListOf<Double>()
        var projectedPrice = currentPrice
        
        for (i in 0 until OUTPUT_SIZE) {
            // Apply a slight random noise + trend direction
            val noise = (Math.random() - 0.5) * (atr * 0.2)
            projectedPrice = (projectedPrice * trendMultiplier) + noise
            predictedPrices.add(projectedPrice)
        }

        return PredictionResult(predictedPrices, finalSignal, finalConfidence)
    }
}
