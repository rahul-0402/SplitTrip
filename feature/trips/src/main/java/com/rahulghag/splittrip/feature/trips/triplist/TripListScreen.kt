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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AirplaneTicket
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.rahulghag.splittrip.feature.trips.triplist.model.FakeTripsData
import com.rahulghag.splittrip.feature.trips.triplist.model.TripStatus
import com.rahulghag.splittrip.feature.trips.triplist.model.TripUiModel
import kotlinx.collections.immutable.toPersistentList

@Composable
fun TripListScreen(
    onNavigateToTripDetail: (String) -> Unit,
    onNavigateToCreateTrip: () -> Unit,
    viewModel: TripListViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CollectEvents(viewModel.uiEvent) { event ->
        when (event) {
            is TripListEvent.NavigateToTripDetail ->
                onNavigateToTripDetail(event.tripId)
            TripListEvent.NavigateToCreateTrip ->
                onNavigateToCreateTrip()
        }
    }

    Scaffold(
        topBar = {
            SplitTripTopBar(title = "My trips")
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onIntent(TripListIntent.CreateTripClicked) },
                containerColor = MaterialTheme.colorScheme.primary,
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
                    TripSummaryCard(
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
private fun TripSummaryCard(
    totalYouAreOwed: Double,
    totalYouOwe: Double,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.spaceL),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "YOU ARE OWED",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(Dimens.spaceXS))
                AmountText(
                    amount = totalYouAreOwed,
                    style = AmountTextStyle.copy(fontSize = 20.sp),
                )
            }

            VerticalDivider(modifier = Modifier.height(40.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "YOU OWE",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(Dimens.spaceXS))
                AmountText(
                    amount = totalYouOwe,
                    type = AmountType.NEGATIVE,
                    showSign = false,
                    style = AmountTextStyle.copy(fontSize = 20.sp),
                )
            }
        }
    }
}

@Composable
private fun TripListItem(
    trip: TripUiModel,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceXS)
            .alpha(if (trip.status == TripStatus.ARCHIVED) 0.6f else 1f),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier.padding(Dimens.spaceM),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.spaceM),
        ) {
            // Trip icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.small,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = trip.icon,
                    fontSize = 22.sp,
                )
            }

            // Trip info
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
                        Box(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = MaterialTheme.shapes.extraSmall,
                                )
                                .padding(horizontal = Dimens.spaceXS, vertical = 2.dp),
                        ) {
                            Text(
                                text = "Archived",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
                Text(
                    text = "${trip.memberCount} members · ${trip.expenseCount} expenses",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            // Balance
            Column(horizontalAlignment = Alignment.End) {
                if (trip.yourBalance == 0.0) {
                    val extColors = MaterialTheme.extendedColors
                    Box(
                        modifier = Modifier
                            .background(
                                color = extColors.successContainer,
                                shape = MaterialTheme.shapes.extraSmall,
                            )
                            .padding(horizontal = Dimens.spaceS, vertical = 2.dp),
                    ) {
                        Text(
                            text = "Settled",
                            style = MaterialTheme.typography.labelSmall,
                            color = extColors.success,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                } else {
                    AmountText(
                        amount = trip.yourBalance,
                        style = AmountTextStyle,
                    )
                    Text(
                        text = if (trip.yourBalance > 0) "you get back" else "you owe",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
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
                trips = FakeTripsData.trips.toPersistentList(),
                isLoading = false,
                totalYouAreOwed = 840.0,
                totalYouOwe = 1520.0,
            ),
            onIntent = {},
        )
    }
}
