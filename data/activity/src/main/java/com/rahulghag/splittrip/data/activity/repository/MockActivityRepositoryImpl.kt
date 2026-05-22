package com.rahulghag.splittrip.data.activity.repository

import com.rahulghag.splittrip.data.activity.mock.datasource.MockActivityDataSource
import com.rahulghag.splittrip.domain.activity.model.Notification
import com.rahulghag.splittrip.domain.activity.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockActivityRepositoryImpl(readJson: (String) -> String) : ActivityRepository {

    private val dataSource = MockActivityDataSource(readJson)

    override fun getNotifications(): Flow<List<Notification>> = flow {
        emit(dataSource.getNotifications())
    }

    override suspend fun markAsRead(notificationId: String) = Unit

    override suspend fun markAllAsRead() = Unit
}
