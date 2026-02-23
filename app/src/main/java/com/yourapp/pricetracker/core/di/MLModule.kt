package com.yourapp.pricetracker.core.di

import android.content.Context
import com.yourapp.pricetracker.ml.model.PricePredictionModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MLModule {

    @Provides
    @Singleton
    fun providePricePredictionModel(@ApplicationContext context: Context): PricePredictionModel {
        return PricePredictionModel(context)
    }
}
