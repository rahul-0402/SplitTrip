package com.rahulghag.splittrip.feature.trips.triplist

import com.rahulghag.splittrip.core.common.mvi.UiEvent
import com.rahulghag.splittrip.core.common.mvi.UiIntent
import com.rahulghag.splittrip.core.common.mvi.UiState
import com.rahulghag.splittrip.domain.trips.model.Trip
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class TripListState(
    val trips: ImmutableList<Trip> = persistentListOf(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val totalYouAreOwed: Double = 0.0,
    val totalYouOwe: Double = 0.0,
) : UiState

sealed class TripListIntent : UiIntent {
    data object LoadTrips : TripListIntent()
    data class TripClicked(val tripId: String) : TripListIntent()
    data object CreateTripClicked : TripListIntent()
    data object RetryClicked : TripListIntent()
}

sealed class TripListEvent : UiEvent {
    data class NavigateToTripDetail(val tripId: String) : TripListEvent()
    data object NavigateToCreateTrip : TripListEvent()
}
