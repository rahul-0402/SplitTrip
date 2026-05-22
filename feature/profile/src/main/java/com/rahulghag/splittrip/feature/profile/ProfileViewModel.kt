package com.rahulghag.splittrip.feature.profile

import com.rahulghag.splittrip.core.ui.viewmodel.SplitTripViewModel
import com.rahulghag.splittrip.domain.profile.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : SplitTripViewModel<ProfileState, ProfileIntent, ProfileEvent>(
    initialState = ProfileState()
) {
    init {
        onIntent(ProfileIntent.LoadProfile)
    }

    override fun onIntent(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.LoadProfile -> loadProfile()

            ProfileIntent.EditUpiIdClicked ->
                sendEvent(ProfileEvent.ShowSnackbar("Coming soon"))

            ProfileIntent.SignOutClicked ->
                updateState { copy(showSignOutDialog = true) }

            ProfileIntent.ConfirmSignOut -> {
                updateState { copy(showSignOutDialog = false) }
                sendEvent(ProfileEvent.NavigateToLogin)
            }

            ProfileIntent.DismissSignOutDialog ->
                updateState { copy(showSignOutDialog = false) }

            ProfileIntent.OpenDesignSystemClicked ->
                sendEvent(ProfileEvent.NavigateToDesignSystem)

            ProfileIntent.OpenCounterClicked ->
                sendEvent(ProfileEvent.NavigateToCounter)
        }
    }

    private fun loadProfile() {
        launch {
            updateState { copy(isLoading = true, error = null) }
            val profile = profileRepository.getProfile()
            updateState { copy(profile = profile, isLoading = false) }
        }
    }
}
