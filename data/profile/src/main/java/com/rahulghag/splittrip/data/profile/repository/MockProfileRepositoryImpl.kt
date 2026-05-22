package com.rahulghag.splittrip.data.profile.repository

import com.rahulghag.splittrip.data.profile.mock.datasource.MockProfileDataSource
import com.rahulghag.splittrip.domain.profile.model.Profile
import com.rahulghag.splittrip.domain.profile.repository.ProfileRepository

class MockProfileRepositoryImpl(readJson: (String) -> String) : ProfileRepository {

    private val dataSource = MockProfileDataSource(readJson)

    override suspend fun getProfile(): Profile = dataSource.getProfile()

    override suspend fun updateProfile(profile: Profile) = Unit
}
