package com.rahulghag.splittrip.feature.auth.profilesetup

import com.rahulghag.splittrip.core.ui.viewmodel.SplitTripViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class ProfileSetupViewModel @Inject constructor() :
    SplitTripViewModel<ProfileSetupState, ProfileSetupIntent, ProfileSetupEvent>(
        initialState = ProfileSetupState()
    ) {

    override fun onIntent(intent: ProfileSetupIntent) {
        when (intent) {
            is ProfileSetupIntent.FullNameChanged -> {
                updateState {
                    copy(
                        fullName = intent.name,
                        nameError = null,
                    )
                }
            }

            is ProfileSetupIntent.UpiIdChanged -> {
                updateState { copy(upiId = intent.upiId) }
            }

            ProfileSetupIntent.SaveClicked -> {
                if (validateForm()) {
                    save()
                }
            }

            ProfileSetupIntent.SkipClicked -> {
                sendEvent(ProfileSetupEvent.NavigateToTripList)
            }
        }
    }

    private fun validateForm(): Boolean {
        val nameError = when {
            currentState.fullName.isBlank() -> "Name is required"
            currentState.fullName.trim().length < 2 -> "Name must be at least 2 characters"
            else -> null
        }

        return if (nameError != null) {
            updateState { copy(nameError = nameError) }
            false
        } else {
            true
        }
    }

    private fun save() {
        launch {
            updateState { copy(isLoading = true) }
            // TODO: replace with real profile save later
            delay(1000)
            updateState { copy(isLoading = false) }
            sendEvent(ProfileSetupEvent.NavigateToTripList)
        }
    }
}
