package com.rahulghag.splittrip.feature.profile.repository

import com.rahulghag.splittrip.feature.profile.model.ProfileUiModel

interface ProfileRepository {
    suspend fun getProfile(): ProfileUiModel?
    suspend fun updateProfile(profile: ProfileUiModel)
}
