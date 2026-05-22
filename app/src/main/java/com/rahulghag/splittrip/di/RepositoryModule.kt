package com.rahulghag.splittrip.di

import com.rahulghag.splittrip.BuildConfig
import com.rahulghag.splittrip.data.activity.repository.MockActivityRepositoryImpl
import com.rahulghag.splittrip.data.profile.repository.MockProfileRepositoryImpl
import com.rahulghag.splittrip.data.settle.repository.MockSettlementRepositoryImpl
import com.rahulghag.splittrip.data.trips.repository.MockExpenseRepositoryImpl
import com.rahulghag.splittrip.data.trips.repository.MockTripRepositoryImpl
import com.rahulghag.splittrip.domain.activity.repository.ActivityRepository
import com.rahulghag.splittrip.domain.profile.repository.ProfileRepository
import com.rahulghag.splittrip.domain.settle.repository.SettlementRepository
import com.rahulghag.splittrip.domain.trips.repository.ExpenseRepository
import com.rahulghag.splittrip.domain.trips.repository.TripRepository
import com.rahulghag.splittrip.mock.AssetReader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

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

    @Provides
    @Singleton
    fun provideSettlementRepository(assetReader: AssetReader): SettlementRepository =
        if (BuildConfig.MOCK_MODE) MockSettlementRepositoryImpl(assetReader::read)
        else TODO("Wire RealSettlementRepository")

    @Provides
    @Singleton
    fun provideActivityRepository(assetReader: AssetReader): ActivityRepository =
        if (BuildConfig.MOCK_MODE) MockActivityRepositoryImpl(assetReader::read)
        else TODO("Wire RealActivityRepository")

    @Provides
    @Singleton
    fun provideProfileRepository(assetReader: AssetReader): ProfileRepository =
        if (BuildConfig.MOCK_MODE) MockProfileRepositoryImpl(assetReader::read)
        else TODO("Wire RealProfileRepository")
}
