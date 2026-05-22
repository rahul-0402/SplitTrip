package com.rahulghag.splittrip.data.repository

import com.rahulghag.splittrip.feature.activity.model.NotificationUiModel
import com.rahulghag.splittrip.feature.activity.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ActivityRepository @Inject constructor() : ActivityRepository {

    override fun getNotifications(): Flow<List<NotificationUiModel>> = flow {
        emit(emptyList()) // TODO: Room query
    }

    override suspend fun markAsRead(notificationId: String) = Unit // TODO: Room update

    override suspend fun markAllAsRead() = Unit // TODO: Room update
}
