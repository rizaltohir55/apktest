package com.yourapp.pricetracker.core.di

import com.yourapp.pricetracker.data.remote.repository.AlertRepositoryImpl
import com.yourapp.pricetracker.data.remote.repository.PriceRepositoryImpl
import com.yourapp.pricetracker.data.remote.repository.WatchlistRepositoryImpl
import com.yourapp.pricetracker.domain.repository.IAlertRepository
import com.yourapp.pricetracker.domain.repository.IPriceRepository
import com.yourapp.pricetracker.domain.repository.IWatchlistRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPriceRepository(
        impl: PriceRepositoryImpl
    ): IPriceRepository

    @Binds
    @Singleton
    abstract fun bindAlertRepository(
        impl: AlertRepositoryImpl
    ): IAlertRepository

    @Binds
    @Singleton
    abstract fun bindWatchlistRepository(
        impl: WatchlistRepositoryImpl
    ): IWatchlistRepository
}
