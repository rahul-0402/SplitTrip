package com.rahulghag.splittrip.mock.repository

import com.rahulghag.splittrip.core.common.mock.MockJson
import com.rahulghag.splittrip.feature.profile.mock.ProfileJsonModel
import com.rahulghag.splittrip.feature.profile.model.ProfileUiModel
import com.rahulghag.splittrip.feature.profile.repository.ProfileRepository
import com.rahulghag.splittrip.mock.AssetReader
import javax.inject.Inject

class MockProfileRepository @Inject constructor(
    private val assetReader: AssetReader,
) : ProfileRepository {

    override suspend fun getProfile(): ProfileUiModel? {
        val json = assetReader.read("profile.json")
        return MockJson.decodeFromString<ProfileJsonModel>(json).toUiModel()
    }

    override suspend fun updateProfile(profile: ProfileUiModel) = Unit // stub
}
