package com.rahulghag.splittrip.feature.activity.repository

import com.rahulghag.splittrip.feature.activity.model.NotificationUiModel
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    fun getNotifications(): Flow<List<NotificationUiModel>>
    suspend fun markAsRead(notificationId: String)
    suspend fun markAllAsRead()
}
