package com.yourapp.pricetracker.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TwelveDataPriceDto(
    val price: String
)

@Serializable
data class TwelveDataQuoteDto(
    val symbol: String,
    val name: String,
    val exchange: String,
    val currency: String,
    val datetime: String,
    val open: String,
    val high: String,
    val low: String,
    val close: String,
    val volume: String,
    val previous_close: String,
    val change: String,
    val percent_change: String
)
