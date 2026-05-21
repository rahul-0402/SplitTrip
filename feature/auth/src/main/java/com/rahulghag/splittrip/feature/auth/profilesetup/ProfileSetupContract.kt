package com.rahulghag.splittrip.feature.auth.profilesetup

import com.rahulghag.splittrip.core.common.mvi.UiEvent
import com.rahulghag.splittrip.core.common.mvi.UiIntent
import com.rahulghag.splittrip.core.common.mvi.UiState

data class ProfileSetupState(
    val fullName: String = "",
    val upiId: String = "",
    val isLoading: Boolean = false,
    val nameError: String? = null,
) : UiState

sealed class ProfileSetupIntent : UiIntent {
    data class FullNameChanged(val name: String) : ProfileSetupIntent()
    data class UpiIdChanged(val upiId: String) : ProfileSetupIntent()
    data object SaveClicked : ProfileSetupIntent()
    data object SkipClicked : ProfileSetupIntent()
}

sealed class ProfileSetupEvent : UiEvent {
    data object NavigateToTripList : ProfileSetupEvent()
    data class ShowSnackbar(val message: String) : ProfileSetupEvent()
}
