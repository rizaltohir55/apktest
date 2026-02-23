package com.yourapp.pricetracker.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yourapp.pricetracker.data.local.database.entity.WatchlistEntity

@Dao
interface WatchlistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlist(item: WatchlistEntity)

    @Query("SELECT * FROM watchlist ORDER BY `order` ASC")
    suspend fun getAllWatchlist(): List<WatchlistEntity>
}
