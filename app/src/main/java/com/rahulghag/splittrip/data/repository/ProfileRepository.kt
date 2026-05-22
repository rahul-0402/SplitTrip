package com.rahulghag.splittrip.data.repository

import com.rahulghag.splittrip.feature.profile.model.ProfileUiModel
import com.rahulghag.splittrip.feature.profile.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepository @Inject constructor() : ProfileRepository {

    override suspend fun getProfile(): ProfileUiModel? = null // TODO: Room query

    override suspend fun updateProfile(profile: ProfileUiModel) = Unit // TODO: Room update
}
