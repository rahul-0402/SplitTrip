package com.rahulghag.splittrip.feature.auth.login

import com.rahulghag.splittrip.core.common.mvi.UiEvent
import com.rahulghag.splittrip.core.common.mvi.UiIntent
import com.rahulghag.splittrip.core.common.mvi.UiState

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val generalError: String? = null,
) : UiState {
    // Form is valid when both fields pass validation
    val isFormValid: Boolean
        get() = emailError == null
             && passwordError == null
             && email.isNotEmpty()
             && password.isNotEmpty()
}

sealed class LoginIntent : UiIntent {
    data class EmailChanged(val email: String) : LoginIntent()
    data class PasswordChanged(val password: String) : LoginIntent()
    data object TogglePasswordVisibility : LoginIntent()
    data object SignInClicked : LoginIntent()
    data object GoogleSignInClicked : LoginIntent()
    data object ForgotPasswordClicked : LoginIntent()
    data object SignUpClicked : LoginIntent()
    data object ClearGeneralError : LoginIntent()
}

sealed class LoginEvent : UiEvent {
    data object NavigateToTripList : LoginEvent()
    data object NavigateToProfileSetup : LoginEvent()
    data object NavigateToSignUp : LoginEvent()
    data object NavigateToForgotPassword : LoginEvent()
    data class ShowSnackbar(val message: String) : LoginEvent()
}
