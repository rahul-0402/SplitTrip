package com.rahulghag.splittrip.core.di

import com.rahulghag.splittrip.core.di.mock.AssetReader
import com.rahulghag.splittrip.data.profile.repository.MockProfileRepositoryImpl
import com.rahulghag.splittrip.domain.profile.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileDataModule {

    @Provides
    @Singleton
    fun provideProfileRepository(assetReader: AssetReader): ProfileRepository =
        if (BuildConfig.MOCK_MODE) MockProfileRepositoryImpl(assetReader::read)
        else TODO("Wire RealProfileRepository")
}
