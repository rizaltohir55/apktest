package com.yourapp.pricetracker.ml.indicator

object EMACalculator {
    fun calculate(prices: List<Double>, period: Int): List<Double> {
        if (prices.size < period) return emptyList()
        
        val emaValues = mutableListOf<Double>()
        val multiplier = 2.0 / (period + 1)
        
        // Initial SMA
        var sum = 0.0
        for (i in 0 until period) {
            sum += prices[i]
            emaValues.add(0.0) // Pad
        }
        
        var currentEma = sum / period
        emaValues[period - 1] = currentEma
        
        for (i in period until prices.size) {
            currentEma = (prices[i] - currentEma) * multiplier + currentEma
            emaValues.add(currentEma)
        }
        
        return emaValues
    }
}
