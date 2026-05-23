package com.rahulghag.splittrip.feature.auth.forgotpassword

import com.rahulghag.splittrip.core.common.extensions.isValidEmail
import com.rahulghag.splittrip.domain.auth.repository.SessionRepository
import com.rahulghag.splittrip.core.common.result.onError
import com.rahulghag.splittrip.core.common.result.onSuccess
import com.rahulghag.splittrip.core.ui.viewmodel.SplitTripViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
) : SplitTripViewModel<ForgotPasswordState, ForgotPasswordIntent, ForgotPasswordEvent>(
    initialState = ForgotPasswordState()
) {

    override fun onIntent(intent: ForgotPasswordIntent) {
        when (intent) {
            is ForgotPasswordIntent.EmailChanged -> updateState {
                copy(email = intent.email, emailError = null, generalError = null, successMessage = null)
            }

            ForgotPasswordIntent.SendResetEmailClicked -> {
                if (validateEmail()) sendResetEmail()
            }

            ForgotPasswordIntent.NavigateUpClicked -> sendEvent(ForgotPasswordEvent.NavigateUp)

            ForgotPasswordIntent.ClearGeneralError -> updateState { copy(generalError = null) }
        }
    }

    private fun validateEmail(): Boolean {
        val emailError = when {
            currentState.email.isBlank() -> "Email is required"
            !currentState.email.isValidEmail() -> "Please enter a valid email"
            else -> null
        }
        if (emailError != null) {
            updateState { copy(emailError = emailError) }
            return false
        }
        return true
    }

    private fun sendResetEmail() {
        launch {
            updateState { copy(isLoading = true, generalError = null, successMessage = null) }
            sessionRepository.sendPasswordResetEmail(currentState.email)
                .onSuccess {
                    updateState {
                        copy(
                            isLoading = false,
                            successMessage = "Reset email sent. Check your inbox.",
                        )
                    }
                }
                .onError { _, message ->
                    updateState {
                        copy(
                            isLoading = false,
                            generalError = message ?: "Something went wrong",
                        )
                    }
                }
        }
    }
}
