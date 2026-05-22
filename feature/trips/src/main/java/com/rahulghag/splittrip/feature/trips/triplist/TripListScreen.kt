package com.rahulghag.splittrip.feature.trips.triplist

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AirplaneTicket
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rahulghag.splittrip.core.ui.theme.JetBrainsMono
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rahulghag.splittrip.core.ui.components.empty.EmptyState
import com.rahulghag.splittrip.core.ui.components.loading.SkeletonTripRow
import com.rahulghag.splittrip.core.ui.components.text.AmountText
import com.rahulghag.splittrip.core.ui.components.text.AmountType
import com.rahulghag.splittrip.core.ui.components.topbar.SplitTripTopBar
import com.rahulghag.splittrip.core.ui.extensions.CollectEvents
import com.rahulghag.splittrip.core.ui.theme.AmountTextStyle
import com.rahulghag.splittrip.core.ui.theme.Dimens
import com.rahulghag.splittrip.core.ui.theme.SplitTripTheme
import com.rahulghag.splittrip.core.ui.theme.extendedColors
import com.rahulghag.splittrip.feature.trips.createtrip.CreateTripBottomSheet
import com.rahulghag.splittrip.feature.trips.model.TripStatus
import com.rahulghag.splittrip.feature.trips.model.TripUiModel
import kotlinx.collections.immutable.persistentListOf

@Composable
fun TripListScreen(
    onNavigateToTripDetail: (String) -> Unit,
    viewModel: TripListViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showCreateSheet by remember { mutableStateOf(false) }

    CollectEvents(viewModel.uiEvent) { event ->
        when (event) {
            is TripListEvent.NavigateToTripDetail ->
                onNavigateToTripDetail(event.tripId)
            TripListEvent.NavigateToCreateTrip -> Unit
        }
    }

    Scaffold(
        topBar = {
            SplitTripTopBar(title = "My trips")
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateSheet = true },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
                modifier = Modifier.size(56.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Create trip",
                    tint = Color.White,
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        TripListContent(
            state = state,
            onIntent = viewModel::onIntent,
            modifier = Modifier.padding(padding),
        )
    }

    if (showCreateSheet) {
        CreateTripBottomSheet(
            onDismiss = { showCreateSheet = false },
            onTripCreated = { tripId ->
                showCreateSheet = false
                onNavigateToTripDetail(tripId)
            },
        )
    }
}

@Composable
private fun TripListContent(
    state: TripListState,
    onIntent: (TripListIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        state.isLoading -> {
            Column(modifier = modifier.fillMaxWidth()) {
                repeat(4) { SkeletonTripRow() }
            }
        }

        state.trips.isEmpty() -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                EmptyState(
                    icon = Icons.Outlined.AirplaneTicket,
                    title = "No trips yet",
                    subtitle = "Create your first trip and invite friends to split costs",
                    actionLabel = "Create trip",
                    onAction = { onIntent(TripListIntent.CreateTripClicked) },
                )
            }
        }

        else -> {
            LazyColumn(modifier = modifier.fillMaxSize()) {
                item {
                    TripSummaryRow(
                        totalYouAreOwed = state.totalYouAreOwed,
                        totalYouOwe = state.totalYouOwe,
                    )
                }
                items(state.trips, key = { it.id }) { trip ->
                    TripListItem(
                        trip = trip,
                        onClick = { onIntent(TripListIntent.TripClicked(trip.id)) },
                    )
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
private fun TripSummaryRow(
    totalYouAreOwed: Double,
    totalYouOwe: Double,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.spaceL, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "OWED TO YOU",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            )
            AmountText(
                amount = totalYouAreOwed,
                style = AmountTextStyle.copy(fontSize = 22.sp),
            )
        }
        VerticalDivider(
            modifier = Modifier.height(32.dp),
            color = MaterialTheme.colorScheme.outlineVariant,
        )
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                text = "YOU OWE",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            )
            AmountText(
                amount = totalYouOwe,
                type = AmountType.NEGATIVE,
                showSign = false,
                style = AmountTextStyle.copy(fontSize = 22.sp),
            )
        }
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
}

@Composable
private fun TripListItem(
    trip: TripUiModel,
    onClick: () -> Unit,
) {
    Column(modifier = Modifier.alpha(if (trip.status == TripStatus.ARCHIVED) 0.5f else 1f)) {
        Surface(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Row(
                modifier = Modifier.padding(horizontal = Dimens.spaceL, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.spaceM),
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            shape = MaterialTheme.shapes.small,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = trip.icon, fontSize = 18.sp)
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Dimens.spaceXS),
                    ) {
                        Text(
                            text = trip.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        if (trip.status == TripStatus.ARCHIVED) {
                            Text(
                                text = "Archived",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                    Text(
                        text = "${trip.memberCount} members · ${trip.expenseCount} expenses",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    if (trip.yourBalance == 0.0) {
                        val extColors = MaterialTheme.extendedColors
                        Text(
                            text = "Settled",
                            style = MaterialTheme.typography.labelSmall,
                            color = extColors.success,
                            fontWeight = FontWeight.SemiBold,
                        )
                    } else {
                        AmountText(
                            amount = trip.yourBalance,
                            style = AmountTextStyle.copy(fontSize = 15.sp, fontFamily = JetBrainsMono),
                        )
                        Text(
                            text = if (trip.yourBalance > 0) "you get back" else "you owe",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = Dimens.spaceL),
            color = MaterialTheme.colorScheme.outlineVariant,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TripListLoadingPreview() {
    SplitTripTheme {
        TripListContent(
            state = TripListState(isLoading = true),
            onIntent = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TripListLoadedPreview() {
    SplitTripTheme {
        TripListContent(
            state = TripListState(
                trips = persistentListOf(
                    TripUiModel(
                        id = "trip_1",
                        name = "Goa trip 2025",
                        icon = "✈️",
                        memberCount = 4,
                        expenseCount = 12,
                        totalAmount = 18420.0,
                        yourBalance = 840.0,
                        status = TripStatus.ACTIVE,
                    ),
                    TripUiModel(
                        id = "trip_2",
                        name = "Manali weekend",
                        icon = "🏔️",
                        memberCount = 3,
                        expenseCount = 8,
                        totalAmount = 12600.0,
                        yourBalance = -1200.0,
                        status = TripStatus.ACTIVE,
                    ),
                ),
                isLoading = false,
                totalYouAreOwed = 840.0,
                totalYouOwe = 1520.0,
            ),
            onIntent = {},
        )
    }
}
