package com.yourapp.pricetracker.domain.model

data class TechnicalIndicators(
    val rsi: Double?,
    val macd: MacdResult?,
    val bollingerBands: BollingerBandsResult?,
    val ema20: Double?,
    val ema50: Double?
) {
    companion object {
        fun empty(): TechnicalIndicators = TechnicalIndicators(null, null, null, null, null)
    }
}

data class MacdResult(
    val macdLine: Double,
    val signalLine: Double,
    val histogram: Double
)

data class BollingerBandsResult(
    val upperBand: Double,
    val middleBand: Double,
    val lowerBand: Double
)
