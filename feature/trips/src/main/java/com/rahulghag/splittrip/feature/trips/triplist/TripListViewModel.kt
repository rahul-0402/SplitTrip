package com.rahulghag.splittrip.feature.trips.triplist

import com.rahulghag.splittrip.core.ui.viewmodel.SplitTripViewModel
import com.rahulghag.splittrip.feature.trips.repository.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class TripListViewModel @Inject constructor(
    private val tripRepository: TripRepository,
) : SplitTripViewModel<TripListState, TripListIntent, TripListEvent>(
    initialState = TripListState()
) {

    init {
        onIntent(TripListIntent.LoadTrips)
    }

    override fun onIntent(intent: TripListIntent) {
        when (intent) {
            TripListIntent.LoadTrips,
            TripListIntent.RetryClicked -> loadTrips()

            is TripListIntent.TripClicked ->
                sendEvent(TripListEvent.NavigateToTripDetail(intent.tripId))

            TripListIntent.CreateTripClicked ->
                sendEvent(TripListEvent.NavigateToCreateTrip)
        }
    }

    private fun loadTrips() {
        launch {
            updateState { copy(isLoading = true, error = null) }
            tripRepository.getTrips()
                .catch { e ->
                    updateState {
                        copy(
                            isLoading = false,
                            error = e.message ?: "Failed to load trips",
                        )
                    }
                }
                .collect { trips ->
                    updateState {
                        copy(
                            trips = trips.toImmutableList(),
                            isLoading = false,
                            totalYouAreOwed = trips
                                .filter { it.yourBalance > 0 }
                                .sumOf { it.yourBalance },
                            totalYouOwe = trips
                                .filter { it.yourBalance < 0 }
                                .sumOf { kotlin.math.abs(it.yourBalance) },
                        )
                    }
                }
        }
    }
}
