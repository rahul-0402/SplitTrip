package com.rahulghag.splittrip.core.data.di

import com.rahulghag.splittrip.core.common.repository.SessionRepository
import com.rahulghag.splittrip.core.data.BuildConfig
import com.rahulghag.splittrip.core.data.auth.MockSessionRepository
import com.rahulghag.splittrip.core.data.auth.SupabaseSessionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideSessionRepository(
        supabaseRepo: SupabaseSessionRepository,
        mockRepo: MockSessionRepository,
    ): SessionRepository =
        if (BuildConfig.MOCK_MODE) mockRepo else supabaseRepo
}
