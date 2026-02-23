package com.yourapp.pricetracker.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.yourapp.pricetracker.data.local.database.dao.AlertDao
import com.yourapp.pricetracker.data.local.database.dao.PriceDao
import com.yourapp.pricetracker.data.local.database.dao.WatchlistDao
import com.yourapp.pricetracker.data.local.database.entity.AlertEntity
import com.yourapp.pricetracker.data.local.database.entity.PriceEntity
import com.yourapp.pricetracker.data.local.database.entity.WatchlistEntity
import com.yourapp.pricetracker.domain.model.AssetCategory

class Converters {
    @TypeConverter fun fromCategory(value: AssetCategory): String = value.name
    @TypeConverter fun toCategory(value: String): AssetCategory = AssetCategory.valueOf(value)
}

@Database(
    entities = [PriceEntity::class, WatchlistEntity::class, AlertEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val priceDao: PriceDao
    abstract val watchlistDao: WatchlistDao
    abstract val alertDao: AlertDao
}
