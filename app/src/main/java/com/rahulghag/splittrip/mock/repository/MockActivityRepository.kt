package com.rahulghag.splittrip.mock.repository

import com.rahulghag.splittrip.core.common.mock.MockJson
import com.rahulghag.splittrip.feature.activity.mock.NotificationJsonModel
import com.rahulghag.splittrip.feature.activity.model.NotificationUiModel
import com.rahulghag.splittrip.feature.activity.repository.ActivityRepository
import com.rahulghag.splittrip.mock.AssetReader
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MockActivityRepository @Inject constructor(
    private val assetReader: AssetReader,
) : ActivityRepository {

    override fun getNotifications(): Flow<List<NotificationUiModel>> = flow {
        val json = assetReader.read("activity.json")
        val all = MockJson.decodeFromString<List<NotificationJsonModel>>(json)
        emit(all.map { it.toUiModel() })
    }

    override suspend fun markAsRead(notificationId: String) = Unit // stub

    override suspend fun markAllAsRead() = Unit // stub
}
