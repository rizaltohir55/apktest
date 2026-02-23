package com.yourapp.pricetracker.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yourapp.pricetracker.data.local.database.entity.PriceEntity

@Dao
interface PriceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrices(prices: List<PriceEntity>)

    @Query("SELECT * FROM prices WHERE symbol = :symbol ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentPrices(symbol: String, limit: Int): List<PriceEntity>
}
