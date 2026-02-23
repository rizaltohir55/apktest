package com.yourapp.pricetracker.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

enum class AlertType {
    PRICE_ABOVE, PRICE_BELOW, PERCENT_CHANGE_UP, PERCENT_CHANGE_DOWN,
    RSI_OVERBOUGHT, RSI_OVERSOLD, MACD_BULLISH_CROSS, MACD_BEARISH_CROSS,
    BB_UPPER_BREAK, BB_LOWER_BREAK, VOLUME_SPIKE
}

@Entity(tableName = "alerts")
data class AlertEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val symbol: String,
    val alertType: AlertType,
    val targetValue: Double,
    val isActive: Boolean = true,
    val notificationTitle: String,
    val createdAt: Long = System.currentTimeMillis()
)
