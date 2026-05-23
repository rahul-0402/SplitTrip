package com.rahulghag.splittrip.feature.auth.login

import com.rahulghag.splittrip.core.common.extensions.isValidEmail
import com.rahulghag.splittrip.domain.auth.repository.SessionRepository
import com.rahulghag.splittrip.core.common.result.onError
import com.rahulghag.splittrip.core.common.result.onSuccess
import com.rahulghag.splittrip.core.ui.viewmodel.SplitTripViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
) : SplitTripViewModel<LoginState, LoginIntent, LoginEvent>(
    initialState = LoginState()
) {

    override fun onIntent(intent: LoginIntent) {
        when (intent) {

            is LoginIntent.EmailChanged -> {
                updateState {
                    copy(
                        email = intent.email,
                        // Clear error as user types
                        emailError = null,
                        generalError = null,
                    )
                }
            }

            is LoginIntent.PasswordChanged -> {
                updateState {
                    copy(
                        password = intent.password,
                        passwordError = null,
                        generalError = null,
                    )
                }
            }

            LoginIntent.TogglePasswordVisibility -> {
                updateState {
                    copy(isPasswordVisible = !isPasswordVisible)
                }
            }

            LoginIntent.SignInClicked -> {
                if (validateForm()) {
                    signIn()
                }
            }

            LoginIntent.GoogleSignInClicked -> {
                // TODO: wire Supabase Google SSO later
                sendEvent(
                    LoginEvent.ShowSnackbar(
                        "Google Sign-In coming soon!"
                    )
                )
            }

            LoginIntent.ForgotPasswordClicked -> {
                sendEvent(LoginEvent.NavigateToForgotPassword)
            }

            LoginIntent.SignUpClicked -> {
                sendEvent(LoginEvent.NavigateToSignUp)
            }

            LoginIntent.ClearGeneralError -> {
                updateState { copy(generalError = null) }
            }
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        val emailError = when {
            currentState.email.isBlank() ->
                "Email is required"
            !currentState.email.isValidEmail() ->
                "Please enter a valid email"
            else -> null
        }

        val passwordError = when {
            currentState.password.isBlank() ->
                "Password is required"
            currentState.password.length < 6 ->
                "Password must be at least 6 characters"
            else -> null
        }

        if (emailError != null || passwordError != null) {
            updateState {
                copy(
                    emailError = emailError,
                    passwordError = passwordError,
                )
            }
            isValid = false
        }

        return isValid
    }

    private fun signIn() {
        launch {
            updateState { copy(isLoading = true, generalError = null) }
            sessionRepository.signInWithEmail(currentState.email, currentState.password)
                .onSuccess {
                    updateState { copy(isLoading = false) }
                    sendEvent(LoginEvent.NavigateToTripList)
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
