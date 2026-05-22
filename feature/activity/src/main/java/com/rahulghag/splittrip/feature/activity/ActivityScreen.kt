package com.rahulghag.splittrip.feature.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rahulghag.splittrip.core.ui.components.empty.EmptyState
import com.rahulghag.splittrip.core.ui.components.loading.SkeletonBox
import com.rahulghag.splittrip.core.ui.components.loading.shimmerBrush
import com.rahulghag.splittrip.core.ui.components.topbar.SplitTripTopBar
import com.rahulghag.splittrip.core.ui.extensions.CollectEvents
import com.rahulghag.splittrip.core.ui.theme.Dimens
import com.rahulghag.splittrip.core.ui.theme.SplitTripTheme
import com.rahulghag.splittrip.core.ui.theme.extendedColors
import com.rahulghag.splittrip.feature.activity.model.NotificationType
import com.rahulghag.splittrip.feature.activity.model.NotificationUiModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
fun ActivityScreen(
    onNavigateToTripDetail: (String) -> Unit,
    viewModel: ActivityViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CollectEvents(viewModel.uiEvent) { event ->
        when (event) {
            is ActivityEvent.NavigateToTripDetail -> onNavigateToTripDetail(event.tripId)
            is ActivityEvent.ShowSnackbar -> Unit
        }
    }

    Scaffold(
        topBar = {
            SplitTripTopBar(
                title = "Activity",
                actions = {
                    if (state.unreadCount > 0) {
                        TextButton(
                            onClick = { viewModel.onIntent(ActivityIntent.MarkAllReadClicked) },
                        ) {
                            Text(
                                text = "Mark all read",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        ActivityContent(
            state = state,
            onIntent = viewModel::onIntent,
            modifier = Modifier.padding(padding),
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ActivityContent(
    state: ActivityState,
    onIntent: (ActivityIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.isLoading) {
        Column(modifier = modifier.fillMaxWidth()) {
            repeat(5) { SkeletonNotificationRow() }
        }
        return
    }

    if (state.notifications.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            EmptyState(
                icon = Icons.Outlined.Notifications,
                title = "No activity yet",
                subtitle = "Notifications about your trips will appear here",
            )
        }
        return
    }

    LazyColumn(modifier = modifier.fillMaxSize()) {
        state.groupedNotifications.forEach { (dateLabel, notifs) ->
            stickyHeader(key = "header_$dateLabel") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(
                            horizontal = Dimens.spaceL,
                            vertical = Dimens.spaceS,
                        ),
                ) {
                    Text(
                        text = dateLabel,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, letterSpacing = 1.5.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    )
                }
            }
            items(notifs, key = { it.id }) { notification ->
                NotificationRow(
                    notification = notification,
                    onClick = { onIntent(ActivityIntent.NotificationClicked(notification)) },
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = Dimens.spaceL),
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
            }
        }
        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
private fun NotificationRow(
    notification: NotificationUiModel,
    onClick: () -> Unit,
) {
    val extColors = MaterialTheme.extendedColors
    val (icon, tint) = notificationIconAndTint(notification.type, extColors)

    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        color = if (!notification.isRead)
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        else
            MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM),
            verticalAlignment = Alignment.Top,
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(
                        if (!notification.isRead) MaterialTheme.colorScheme.primary
                        else Color.Transparent,
                    ),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = Dimens.spaceM),
            ) {
                Text(
                    text = notification.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (!notification.isRead)
                        MaterialTheme.colorScheme.onSurface
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(Dimens.spaceXS))
                Text(
                    text = notification.body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(Modifier.width(Dimens.spaceM))
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = tint,
            )
        }
    }
}

@Composable
private fun SkeletonNotificationRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(Dimens.spaceM),
    ) {
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .size(7.dp)
                .clip(CircleShape)
                .background(shimmerBrush()),
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Dimens.spaceXS),
        ) {
            SkeletonBox(modifier = Modifier.fillMaxWidth(0.6f), height = 14.dp)
            SkeletonBox(modifier = Modifier.fillMaxWidth(0.8f), height = 11.dp)
        }
        SkeletonBox(width = 20.dp, height = 20.dp)
    }
}

@Composable
private fun notificationIconAndTint(
    type: NotificationType,
    extColors: com.rahulghag.splittrip.core.ui.theme.SplitTripExtendedColors,
): Pair<ImageVector, Color> = when (type) {
    NotificationType.EXPENSE_ADDED,
    NotificationType.EXPENSE_EDITED,
    NotificationType.EXPENSE_DELETED ->
        Icons.Outlined.ReceiptLong to MaterialTheme.colorScheme.primary

    NotificationType.SETTLEMENT_CONFIRMED,
    NotificationType.SETTLEMENT_REQUESTED ->
        Icons.Outlined.CheckCircle to extColors.success

    NotificationType.MEMBER_JOINED ->
        Icons.Outlined.PersonAdd to extColors.info

    NotificationType.TRIP_ARCHIVED ->
        Icons.Outlined.Archive to MaterialTheme.colorScheme.onSurfaceVariant
}

@Preview(showBackground = true, name = "Loading")
@Composable
private fun ActivityLoadingPreview() {
    SplitTripTheme {
        ActivityContent(
            state = ActivityState(isLoading = true),
            onIntent = {},
        )
    }
}

@Preview(showBackground = true, name = "Loaded with notifications")
@Composable
private fun ActivityLoadedPreview() {
    SplitTripTheme {
        ActivityContent(
            state = ActivityState(
                notifications = listOf(
                    NotificationUiModel(
                        id = "notif_1",
                        type = NotificationType.EXPENSE_ADDED,
                        title = "Komal added an expense",
                        body = "Breakfast buffet · ₹1,200 in Goa trip 2025",
                        tripId = "trip_1",
                        isRead = false,
                        createdAt = "2025-01-16T08:30:00Z",
                    ),
                    NotificationUiModel(
                        id = "notif_2",
                        type = NotificationType.SETTLEMENT_CONFIRMED,
                        title = "Arun settled up",
                        body = "Arun paid you ₹320 in Goa trip 2025",
                        tripId = "trip_1",
                        isRead = false,
                        createdAt = "2025-01-15T14:20:00Z",
                    ),
                    NotificationUiModel(
                        id = "notif_3",
                        type = NotificationType.MEMBER_JOINED,
                        title = "Sara joined Goa trip 2025",
                        body = "Sara accepted your invite",
                        tripId = "trip_1",
                        isRead = true,
                        createdAt = "2025-01-14T10:00:00Z",
                    ),
                ).toImmutableList(),
                unreadCount = 2,
                isLoading = false,
            ),
            onIntent = {},
        )
    }
}
