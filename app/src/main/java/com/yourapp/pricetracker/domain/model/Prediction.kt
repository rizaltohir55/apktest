package com.yourapp.pricetracker.domain.model

enum class Signal {
    BUY, SELL, HOLD
}

data class PredictionResult(
    val predictedPrices: List<Double>,
    val signal: Signal,
    val confidence: Float
)
