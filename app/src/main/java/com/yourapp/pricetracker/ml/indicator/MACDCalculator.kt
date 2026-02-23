package com.yourapp.pricetracker.ml.indicator

import com.yourapp.pricetracker.domain.model.MacdResult

object MACDCalculator {
    fun calculate(
        prices: List<Double>,
        shortPeriod: Int = 12,
        longPeriod: Int = 26,
        signalPeriod: Int = 9
    ): List<MacdResult?> {
        if (prices.size < longPeriod + signalPeriod) return emptyList()
        
        val shortEma = EMACalculator.calculate(prices, shortPeriod)
        val longEma = EMACalculator.calculate(prices, longPeriod)
        
        val macdLine = mutableListOf<Double>()
        for (i in prices.indices) {
            if (i < longPeriod - 1) {
                macdLine.add(0.0)
            } else {
                macdLine.add(shortEma[i] - longEma[i])
            }
        }
        
        val signalLineElements = EMACalculator.calculate(macdLine.drop(longPeriod - 1), signalPeriod)
        val paddedSignal = List(longPeriod - 1 + signalPeriod - 1) { 0.0 } + signalLineElements
        
        val results = mutableListOf<MacdResult?>()
        for (i in prices.indices) {
            if (i < paddedSignal.size || paddedSignal[i] == 0.0) {
                results.add(null)
            } else {
                val macd = macdLine[i]
                val signal = paddedSignal[i]
                results.add(MacdResult(macd, signal, macd - signal))
            }
        }
        return results
    }
}
