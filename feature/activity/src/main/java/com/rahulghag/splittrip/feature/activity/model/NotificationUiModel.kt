package com.rahulghag.splittrip.feature.activity.model

data class NotificationUiModel(
    val id: String,
    val type: NotificationType,
    val title: String,
    val body: String,
    val tripId: String?,
    val isRead: Boolean,
    val createdAt: String,
)

enum class NotificationType {
    EXPENSE_ADDED,
    EXPENSE_EDITED,
    EXPENSE_DELETED,
    SETTLEMENT_CONFIRMED,
    SETTLEMENT_REQUESTED,
    MEMBER_JOINED,
    TRIP_ARCHIVED,
}
