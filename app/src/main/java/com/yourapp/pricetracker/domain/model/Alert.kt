package com.yourapp.pricetracker.domain.model

data class Alert(
    val id: String,
    val symbol: String,
    val isTriggered: Boolean
) {
    fun isTriggered(price: Price): Boolean {
        // Placeholder implementation
        return false
    }
}
