package com.yourapp.pricetracker.domain.model

data class Asset(
    val symbol: String,
    val name: String,
    val category: AssetCategory
)
