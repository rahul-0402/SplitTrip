package com.rahulghag.splittrip.feature.trips.tripmembers

import com.rahulghag.splittrip.core.common.mvi.UiEvent
import com.rahulghag.splittrip.core.common.mvi.UiIntent
import com.rahulghag.splittrip.core.common.mvi.UiState
import com.rahulghag.splittrip.feature.trips.model.TripMemberUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class TripMembersState(
    val tripId: String = "",
    val tripName: String = "",
    val members: ImmutableList<TripMemberUiModel> = persistentListOf(),
    val inviteCode: String = "",
    val inviteLink: String = "",
    val isLoading: Boolean = true,
    val isInviteLinkCopied: Boolean = false,
    val error: String? = null,
) : UiState

sealed class TripMembersIntent : UiIntent {
    data class LoadMembers(val tripId: String, val tripName: String) : TripMembersIntent()
    data object CopyInviteLinkClicked : TripMembersIntent()
    data object ShareInviteLinkClicked : TripMembersIntent()
    data object RetryClicked : TripMembersIntent()
}

sealed class TripMembersEvent : UiEvent {
    data object NavigateUp : TripMembersEvent()
    data class CopyToClipboard(val text: String) : TripMembersEvent()
    data class ShareText(val text: String) : TripMembersEvent()
    data class ShowSnackbar(val message: String) : TripMembersEvent()
}
