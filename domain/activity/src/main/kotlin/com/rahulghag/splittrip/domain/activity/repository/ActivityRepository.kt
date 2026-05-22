package com.rahulghag.splittrip.domain.activity.repository

import com.rahulghag.splittrip.domain.activity.model.Notification
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    fun getNotifications(): Flow<List<Notification>>
    suspend fun markAsRead(notificationId: String)
    suspend fun markAllAsRead()
}
