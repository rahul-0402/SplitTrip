package com.rahulghag.splittrip.feature.profile

import com.rahulghag.splittrip.core.common.mvi.UiEvent
import com.rahulghag.splittrip.core.common.mvi.UiIntent
import com.rahulghag.splittrip.core.common.mvi.UiState
import com.rahulghag.splittrip.domain.profile.model.Profile

data class ProfileState(
    val profile: Profile? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val showSignOutDialog: Boolean = false,
) : UiState

sealed class ProfileIntent : UiIntent {
    data object LoadProfile : ProfileIntent()
    data object EditUpiIdClicked : ProfileIntent()
    data object SignOutClicked : ProfileIntent()
    data object ConfirmSignOut : ProfileIntent()
    data object DismissSignOutDialog : ProfileIntent()
    data object OpenDesignSystemClicked : ProfileIntent()
    data object OpenCounterClicked : ProfileIntent()
}

sealed class ProfileEvent : UiEvent {
    data object NavigateToLogin : ProfileEvent()
    data object NavigateToDesignSystem : ProfileEvent()
    data object NavigateToCounter : ProfileEvent()
    data class ShowSnackbar(val message: String) : ProfileEvent()
}
