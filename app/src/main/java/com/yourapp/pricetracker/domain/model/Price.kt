package com.yourapp.pricetracker.domain.model

data class Price(
    val symbol: String,
    val current: Double,
    val change: Double,
    val changePercent: Double,
    val timestamp: Long
)
