@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.rahulghag.splittrip.data.activity.mock.datasource

import com.rahulghag.splittrip.core.common.mock.MockJson
import com.rahulghag.splittrip.data.activity.mock.model.NotificationJsonModel
import com.rahulghag.splittrip.domain.activity.model.Notification

class MockActivityDataSource(private val readJson: (String) -> String) {

    fun getNotifications(): List<Notification> {
        val json = readJson("activity.json")
        return MockJson.decodeFromString<List<NotificationJsonModel>>(json).map { it.toModel() }
    }
}
