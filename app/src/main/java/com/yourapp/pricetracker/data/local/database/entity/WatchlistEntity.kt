package com.yourapp.pricetracker.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yourapp.pricetracker.domain.model.AssetCategory

@Entity(tableName = "watchlist")
data class WatchlistEntity(
    @PrimaryKey val symbol: String,
    val name: String,
    val category: AssetCategory,
    val order: Int,
    val addedAt: Long = System.currentTimeMillis()
)
