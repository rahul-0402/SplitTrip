package com.rahulghag.splittrip.core.di

import com.rahulghag.splittrip.core.di.mock.AssetReader
import com.rahulghag.splittrip.data.activity.repository.MockActivityRepositoryImpl
import com.rahulghag.splittrip.domain.activity.repository.ActivityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ActivityDataModule {

    @Provides
    @Singleton
    fun provideActivityRepository(assetReader: AssetReader): ActivityRepository =
        if (BuildConfig.MOCK_MODE) MockActivityRepositoryImpl(assetReader::read)
        else TODO("Wire RealActivityRepository")
}
