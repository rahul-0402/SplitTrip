package com.rahulghag.splittrip.di

import com.rahulghag.splittrip.BuildConfig
import com.rahulghag.splittrip.feature.activity.repository.ActivityRepository
import com.rahulghag.splittrip.feature.profile.repository.ProfileRepository
import com.rahulghag.splittrip.feature.settle.repository.SettlementRepository
import com.rahulghag.splittrip.feature.trips.repository.ExpenseRepository
import com.rahulghag.splittrip.feature.trips.repository.TripRepository
import com.rahulghag.splittrip.mock.AssetReader
import com.rahulghag.splittrip.mock.repository.MockActivityRepository
import com.rahulghag.splittrip.mock.repository.MockExpenseRepository
import com.rahulghag.splittrip.mock.repository.MockProfileRepository
import com.rahulghag.splittrip.mock.repository.MockSettlementRepository
import com.rahulghag.splittrip.mock.repository.MockTripRepository
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
    fun provideTripRepository(
        assetReader: AssetReader,
        tripRepository: TripRepository,
    ): TripRepository =
        if (BuildConfig.MOCK_MODE)
            MockTripRepository(assetReader)
        else
            tripRepository

    @Provides
    @Singleton
    fun provideExpenseRepository(
        assetReader: AssetReader,
        expenseRepository: ExpenseRepository,
    ): ExpenseRepository =
        if (BuildConfig.MOCK_MODE)
            MockExpenseRepository(assetReader)
        else
            expenseRepository

    @Provides
    @Singleton
    fun provideSettlementRepository(
        assetReader: AssetReader,
        settlementRepository: SettlementRepository,
    ): SettlementRepository =
        if (BuildConfig.MOCK_MODE)
            MockSettlementRepository(assetReader)
        else
            settlementRepository

    @Provides
    @Singleton
    fun provideActivityRepository(
        assetReader: AssetReader,
        activityRepository: ActivityRepository,
    ): ActivityRepository =
        if (BuildConfig.MOCK_MODE)
            MockActivityRepository(assetReader)
        else
            activityRepository

    @Provides
    @Singleton
    fun provideProfileRepository(
        assetReader: AssetReader,
        profileRepository: ProfileRepository,
    ): ProfileRepository =
        if (BuildConfig.MOCK_MODE)
            MockProfileRepository(assetReader)
        else
            profileRepository
}
