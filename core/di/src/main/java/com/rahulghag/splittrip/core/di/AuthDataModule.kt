package com.rahulghag.splittrip.core.di

import com.rahulghag.splittrip.data.auth.MockSessionRepository
import com.rahulghag.splittrip.data.auth.SupabaseSessionRepository
import com.rahulghag.splittrip.domain.auth.repository.SessionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthDataModule {

    @Provides
    @Singleton
    fun provideSessionRepository(supabase: SupabaseClient): SessionRepository =
        if (BuildConfig.MOCK_MODE) MockSessionRepository()
        else SupabaseSessionRepository(supabase)
}
