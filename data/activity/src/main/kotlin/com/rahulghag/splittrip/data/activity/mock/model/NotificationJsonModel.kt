package com.rahulghag.splittrip.data.activity.mock.model

import com.rahulghag.splittrip.domain.activity.model.Notification
import com.rahulghag.splittrip.domain.activity.model.NotificationType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationJsonModel(
    val id: String,
    val type: String,
    val title: String,
    val body: String,
    @SerialName("trip_id") val tripId: String? = null,
    @SerialName("is_read") val isRead: Boolean = false,
    @SerialName("created_at") val createdAt: String,
) {
    fun toModel() = Notification(
        id = id,
        type = when (type) {
            "EXPENSE_ADDED"        -> NotificationType.EXPENSE_ADDED
            "EXPENSE_EDITED"       -> NotificationType.EXPENSE_EDITED
            "EXPENSE_DELETED"      -> NotificationType.EXPENSE_DELETED
            "SETTLEMENT_CONFIRMED" -> NotificationType.SETTLEMENT_CONFIRMED
            "SETTLEMENT_REQUESTED" -> NotificationType.SETTLEMENT_REQUESTED
            "MEMBER_JOINED"        -> NotificationType.MEMBER_JOINED
            "TRIP_ARCHIVED"        -> NotificationType.TRIP_ARCHIVED
            else                   -> NotificationType.EXPENSE_ADDED
        },
        title = title,
        body = body,
        tripId = tripId,
        isRead = isRead,
        createdAt = createdAt,
    )
}
