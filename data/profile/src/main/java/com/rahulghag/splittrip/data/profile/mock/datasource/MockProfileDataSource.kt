@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.rahulghag.splittrip.data.profile.mock.datasource

import com.rahulghag.splittrip.core.common.mock.MockJson
import com.rahulghag.splittrip.data.profile.mock.model.ProfileJsonModel
import com.rahulghag.splittrip.domain.profile.model.Profile

class MockProfileDataSource(private val readJson: (String) -> String) {

    fun getProfile(): Profile {
        val json = readJson("profile.json")
        return MockJson.decodeFromString<ProfileJsonModel>(json).toModel()
    }
}
