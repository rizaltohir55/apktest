package com.yourapp.pricetracker.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TickerDto(
    @SerialName("s") val symbol: String,
    @SerialName("c") val currentPrice: String,
    @SerialName("p") val priceChange: String,
    @SerialName("P") val priceChangePercent: String,
    @SerialName("E") val eventTime: Long
)

// Wrapper for the Binance WebSocket Stream payload which often looks like {"stream":"...", "data": {...}}
@Serializable
data class BinanceStreamPayload(
    val stream: String,
    val data: TickerDto
)
