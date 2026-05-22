package com.rahulghag.splittrip.feature.activity

import com.rahulghag.splittrip.core.ui.viewmodel.SplitTripViewModel
import com.rahulghag.splittrip.feature.activity.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val activityRepository: ActivityRepository,
) : SplitTripViewModel<ActivityState, ActivityIntent, ActivityEvent>(
    initialState = ActivityState()
) {
    init {
        onIntent(ActivityIntent.LoadNotifications)
    }

    override fun onIntent(intent: ActivityIntent) {
        when (intent) {
            ActivityIntent.LoadNotifications,
            ActivityIntent.RetryClicked -> loadNotifications()

            is ActivityIntent.NotificationClicked -> handleNotificationClick(intent.notification)

            ActivityIntent.MarkAllReadClicked -> markAllRead()
        }
    }

    private fun loadNotifications() {
        launch {
            updateState { copy(isLoading = true, error = null) }
            activityRepository.getNotifications()
                .catch { e ->
                    updateState { copy(isLoading = false, error = e.message ?: "Failed to load") }
                }
                .collect { list ->
                    updateState {
                        copy(
                            notifications = list.toImmutableList(),
                            unreadCount = list.count { !it.isRead },
                            isLoading = false,
                        )
                    }
                }
        }
    }

    private fun handleNotificationClick(notification: com.rahulghag.splittrip.feature.activity.model.NotificationUiModel) {
        if (!notification.isRead) {
            launch {
                activityRepository.markAsRead(notification.id)
                updateState {
                    copy(
                        notifications = notifications.map {
                            if (it.id == notification.id) it.copy(isRead = true) else it
                        }.toImmutableList(),
                        unreadCount = maxOf(0, unreadCount - 1),
                    )
                }
            }
        }
        if (notification.tripId != null) {
            sendEvent(ActivityEvent.NavigateToTripDetail(notification.tripId))
        }
    }

    private fun markAllRead() {
        launch {
            activityRepository.markAllAsRead()
            updateState {
                copy(
                    notifications = notifications.map { it.copy(isRead = true) }.toImmutableList(),
                    unreadCount = 0,
                )
            }
        }
    }
}
