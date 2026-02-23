package com.yourapp.pricetracker.ml.indicator

object RSICalculator {
    fun calculate(prices: List<Double>, period: Int = 14): List<Double> {
        if (prices.size < period + 1) return emptyList()
        val gains = mutableListOf<Double>()
        val losses = mutableListOf<Double>()
        
        for (i in 1 until prices.size) {
            val change = prices[i] - prices[i - 1]
            gains.add(if (change > 0) change else 0.0)
            losses.add(if (change < 0) -change else 0.0)
        }
        
        val rsiValues = mutableListOf<Double>()
        // Pad initial empty values
        for (i in 0 until period) {
            rsiValues.add(0.0)
        }

        var avgGain = gains.take(period).average()
        var avgLoss = losses.take(period).average()
        
        val firstRS = if (avgLoss == 0.0) Double.MAX_VALUE else avgGain / avgLoss
        rsiValues.add(100 - (100 / (1 + firstRS)))
        
        for (i in period until gains.size) {
            avgGain = (avgGain * (period - 1) + gains[i]) / period
            avgLoss = (avgLoss * (period - 1) + losses[i]) / period
            val rs = if (avgLoss == 0.0) Double.MAX_VALUE else avgGain / avgLoss
            rsiValues.add(100 - (100 / (1 + rs)))
        }
        return rsiValues
    }
}
