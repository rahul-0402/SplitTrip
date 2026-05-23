package com.rahulghag.splittrip.core.di

import com.rahulghag.splittrip.core.di.mock.AssetReader
import com.rahulghag.splittrip.data.trips.repository.MockExpenseRepositoryImpl
import com.rahulghag.splittrip.data.trips.repository.MockTripRepositoryImpl
import com.rahulghag.splittrip.domain.trips.repository.ExpenseRepository
import com.rahulghag.splittrip.domain.trips.repository.TripRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TripsDataModule {

    @Provides
    @Singleton
    fun provideTripRepository(assetReader: AssetReader): TripRepository =
        if (BuildConfig.MOCK_MODE) MockTripRepositoryImpl(assetReader::read)
        else TODO("Wire RealTripRepository")

    @Provides
    @Singleton
    fun provideExpenseRepository(assetReader: AssetReader): ExpenseRepository =
        if (BuildConfig.MOCK_MODE) MockExpenseRepositoryImpl(assetReader::read)
        else TODO("Wire RealExpenseRepository")
}
