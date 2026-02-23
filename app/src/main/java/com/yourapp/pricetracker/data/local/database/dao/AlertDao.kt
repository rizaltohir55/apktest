package com.yourapp.pricetracker.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yourapp.pricetracker.data.local.database.entity.AlertEntity

@Dao
interface AlertDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: AlertEntity)

    @Query("SELECT * FROM alerts WHERE symbol = :symbol AND isActive = 1")
    suspend fun getActiveAlerts(symbol: String): List<AlertEntity>
}
