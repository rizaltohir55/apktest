package com.yourapp.pricetracker.core.di

import android.app.Application
import androidx.room.Room
import com.yourapp.pricetracker.data.local.database.AppDatabase
import com.yourapp.pricetracker.data.local.database.dao.AlertDao
import com.yourapp.pricetracker.data.local.database.dao.PriceDao
import com.yourapp.pricetracker.data.local.database.dao.WatchlistDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "price_tracker_db"
        ).build()
    }

    @Provides
    @Singleton
    fun providePriceDao(db: AppDatabase): PriceDao = db.priceDao

    @Provides
    @Singleton
    fun provideWatchlistDao(db: AppDatabase): WatchlistDao = db.watchlistDao

    @Provides
    @Singleton
    fun provideAlertDao(db: AppDatabase): AlertDao = db.alertDao
}
