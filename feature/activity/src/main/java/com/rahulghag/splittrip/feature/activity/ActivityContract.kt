package com.rahulghag.splittrip.feature.activity

import com.rahulghag.splittrip.core.common.mvi.UiEvent
import com.rahulghag.splittrip.core.common.mvi.UiIntent
import com.rahulghag.splittrip.core.common.mvi.UiState
import com.rahulghag.splittrip.feature.activity.model.NotificationUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

data class ActivityState(
    val notifications: ImmutableList<NotificationUiModel> = persistentListOf(),
    val unreadCount: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null,
) : UiState {
    val groupedNotifications: Map<String, List<NotificationUiModel>>
        get() {
            val today = LocalDate.now()
            val yesterday = today.minusDays(1)
            return notifications.groupBy { notification ->
                val datePart = notification.createdAt.substringBefore("T")
                val date = try { LocalDate.parse(datePart) } catch (_: Exception) { null }
                when (date) {
                    today -> "Today"
                    yesterday -> "Yesterday"
                    else -> {
                        val parts = datePart.split("-")
                        if (parts.size == 3) {
                            val month = listOf(
                                "", "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
                            ).getOrElse(parts[1].toIntOrNull() ?: 0) { parts[1] }
                            "${parts[2].toIntOrNull() ?: parts[2]} $month ${parts[0]}"
                        } else datePart
                    }
                }
            }
        }
}

sealed class ActivityIntent : UiIntent {
    data object LoadNotifications : ActivityIntent()
    data class NotificationClicked(val notification: NotificationUiModel) : ActivityIntent()
    data object MarkAllReadClicked : ActivityIntent()
    data object RetryClicked : ActivityIntent()
}

sealed class ActivityEvent : UiEvent {
    data class NavigateToTripDetail(val tripId: String) : ActivityEvent()
    data class ShowSnackbar(val message: String) : ActivityEvent()
}
