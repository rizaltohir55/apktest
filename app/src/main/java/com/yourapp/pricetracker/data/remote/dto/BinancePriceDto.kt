package com.yourapp.pricetracker.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class BinancePriceDto(
    val symbol: String,
    val price: String
)
