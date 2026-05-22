package com.rahulghag.splittrip.domain.profile.repository

import com.rahulghag.splittrip.domain.profile.model.Profile

interface ProfileRepository {
    suspend fun getProfile(): Profile?
    suspend fun updateProfile(profile: Profile)
}
