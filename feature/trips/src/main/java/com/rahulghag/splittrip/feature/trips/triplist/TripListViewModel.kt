package com.rahulghag.splittrip.feature.trips.triplist

import com.rahulghag.splittrip.core.ui.viewmodel.SplitTripViewModel
import com.rahulghag.splittrip.feature.trips.triplist.model.FakeTripsData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class TripListViewModel @Inject constructor() :
    SplitTripViewModel<TripListState, TripListIntent, TripListEvent>(
        initialState = TripListState()
    ) {

    init {
        onIntent(TripListIntent.LoadTrips)
    }

    override fun onIntent(intent: TripListIntent) {
        when (intent) {
            TripListIntent.LoadTrips,
            TripListIntent.RetryClicked -> loadTrips()

            is TripListIntent.TripClicked -> {
                sendEvent(TripListEvent.NavigateToTripDetail(intent.tripId))
            }

            TripListIntent.CreateTripClicked -> {
                sendEvent(TripListEvent.NavigateToCreateTrip)
            }
        }
    }

    private fun loadTrips() {
        launch {
            updateState { copy(isLoading = true, error = null) }
            delay(800)
            val trips = FakeTripsData.trips.toPersistentList()
            val totalYouAreOwed = trips
                .filter { it.yourBalance > 0 }
                .sumOf { it.yourBalance }
            val totalYouOwe = trips
                .filter { it.yourBalance < 0 }
                .sumOf { -it.yourBalance }
            updateState {
                copy(
                    trips = trips,
                    isLoading = false,
                    totalYouAreOwed = totalYouAreOwed,
                    totalYouOwe = totalYouOwe,
                )
            }
        }
    }
}
