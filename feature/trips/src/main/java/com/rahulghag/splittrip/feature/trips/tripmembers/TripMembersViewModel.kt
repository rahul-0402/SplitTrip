package com.rahulghag.splittrip.feature.trips.tripmembers

import androidx.lifecycle.SavedStateHandle
import com.rahulghag.splittrip.core.ui.viewmodel.SplitTripViewModel
import com.rahulghag.splittrip.domain.trips.repository.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class TripMembersViewModel @Inject constructor(
    private val tripRepository: TripRepository,
    savedStateHandle: SavedStateHandle,
) : SplitTripViewModel<TripMembersState, TripMembersIntent, TripMembersEvent>(
    initialState = TripMembersState()
) {
    private val tripId: String = checkNotNull(savedStateHandle["tripId"])
    private val tripName: String = savedStateHandle["tripName"] ?: ""

    init {
        updateState { copy(tripId = tripId, tripName = tripName) }
        onIntent(TripMembersIntent.LoadMembers(tripId, tripName))
    }

    override fun onIntent(intent: TripMembersIntent) {
        when (intent) {
            is TripMembersIntent.LoadMembers -> loadMembers()
            TripMembersIntent.RetryClicked -> loadMembers()

            TripMembersIntent.CopyInviteLinkClicked -> {
                sendEvent(TripMembersEvent.CopyToClipboard(currentState.inviteLink))
                updateState { copy(isInviteLinkCopied = true) }
                launch {
                    delay(3000)
                    updateState { copy(isInviteLinkCopied = false) }
                }
            }

            TripMembersIntent.ShareInviteLinkClicked -> {
                val text = "Join my trip '${currentState.tripName}' on SplitTrip!\n${currentState.inviteLink}"
                sendEvent(TripMembersEvent.ShareText(text))
            }
        }
    }

    private fun loadMembers() {
        launch {
            updateState { copy(isLoading = true, error = null) }
            val trip = tripRepository.getTripById(tripId)
            val members = trip?.members ?: emptyList()
            val inviteCode = trip?.inviteCode ?: ""
            updateState {
                copy(
                    members = members.toImmutableList(),
                    inviteCode = inviteCode,
                    inviteLink = "splittrip.app/join/$inviteCode",
                    isLoading = false,
                )
            }
        }
    }
}
