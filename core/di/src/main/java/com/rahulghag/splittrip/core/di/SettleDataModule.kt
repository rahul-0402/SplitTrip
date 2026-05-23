package com.rahulghag.splittrip.core.di

import com.rahulghag.splittrip.core.di.mock.AssetReader
import com.rahulghag.splittrip.data.settle.repository.MockSettlementRepositoryImpl
import com.rahulghag.splittrip.domain.settle.repository.SettlementRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettleDataModule {

    @Provides
    @Singleton
    fun provideSettlementRepository(assetReader: AssetReader): SettlementRepository =
        if (BuildConfig.MOCK_MODE) MockSettlementRepositoryImpl(assetReader::read)
        else TODO("Wire RealSettlementRepository")
}
