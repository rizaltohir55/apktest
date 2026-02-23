package com.yourapp.pricetracker.ml.indicator

import com.yourapp.pricetracker.domain.model.BollingerBandsResult
import kotlin.math.sqrt

object BollingerBandsCalculator {
    fun calculate(prices: List<Double>, period: Int = 20, multiplier: Double = 2.0): List<BollingerBandsResult?> {
        if (prices.size < period) return emptyList()
        
        val results = mutableListOf<BollingerBandsResult?>()
        
        for (i in prices.indices) {
            if (i < period - 1) {
                results.add(null)
                continue
            }
            
            val slice = prices.subList(i - period + 1, i + 1)
            val mean = slice.average()
            val variance = slice.map { (it - mean) * (it - mean) }.average()
            val stdDev = sqrt(variance)
            
            results.add(
                BollingerBandsResult(
                    upperBand = mean + (multiplier * stdDev),
                    middleBand = mean,
                    lowerBand = mean - (multiplier * stdDev)
                )
            )
        }
        
        return results
    }
}
