package com.rahulghag.splittrip.feature.auth.signup

import com.rahulghag.splittrip.core.common.extensions.isValidEmail
import com.rahulghag.splittrip.domain.auth.repository.SessionRepository
import com.rahulghag.splittrip.core.common.result.onError
import com.rahulghag.splittrip.core.common.result.onSuccess
import com.rahulghag.splittrip.core.ui.viewmodel.SplitTripViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
) : SplitTripViewModel<SignUpState, SignUpIntent, SignUpEvent>(
    initialState = SignUpState()
) {

    override fun onIntent(intent: SignUpIntent) {
        when (intent) {
            is SignUpIntent.EmailChanged -> updateState {
                copy(email = intent.email, emailError = null, generalError = null)
            }

            is SignUpIntent.PasswordChanged -> updateState {
                copy(password = intent.password, passwordError = null, generalError = null)
            }

            SignUpIntent.TogglePasswordVisibility -> updateState {
                copy(isPasswordVisible = !isPasswordVisible)
            }

            SignUpIntent.SignUpClicked -> {
                if (validateForm()) signUp()
            }

            SignUpIntent.SignInClicked -> sendEvent(SignUpEvent.NavigateToLogin)

            SignUpIntent.ClearGeneralError -> updateState { copy(generalError = null) }
        }
    }

    private fun validateForm(): Boolean {
        val emailError = when {
            currentState.email.isBlank() -> "Email is required"
            !currentState.email.isValidEmail() -> "Please enter a valid email"
            else -> null
        }
        val passwordError = when {
            currentState.password.isBlank() -> "Password is required"
            currentState.password.length < 6 -> "Password must be at least 6 characters"
            else -> null
        }
        if (emailError != null || passwordError != null) {
            updateState { copy(emailError = emailError, passwordError = passwordError) }
            return false
        }
        return true
    }

    private fun signUp() {
        launch {
            updateState { copy(isLoading = true, generalError = null) }
            sessionRepository.signUpWithEmail(currentState.email, currentState.password)
                .onSuccess {
                    updateState { copy(isLoading = false) }
                    sendEvent(SignUpEvent.NavigateToProfileSetup)
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
