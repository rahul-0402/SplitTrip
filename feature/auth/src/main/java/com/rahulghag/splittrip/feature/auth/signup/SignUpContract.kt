package com.rahulghag.splittrip.feature.auth.signup

import com.rahulghag.splittrip.core.common.mvi.UiEvent
import com.rahulghag.splittrip.core.common.mvi.UiIntent
import com.rahulghag.splittrip.core.common.mvi.UiState

data class SignUpState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val generalError: String? = null,
) : UiState

sealed class SignUpIntent : UiIntent {
    data class EmailChanged(val email: String) : SignUpIntent()
    data class PasswordChanged(val password: String) : SignUpIntent()
    data object TogglePasswordVisibility : SignUpIntent()
    data object SignUpClicked : SignUpIntent()
    data object SignInClicked : SignUpIntent()
    data object ClearGeneralError : SignUpIntent()
}

sealed class SignUpEvent : UiEvent {
    data object NavigateToProfileSetup : SignUpEvent()
    data object NavigateToLogin : SignUpEvent()
}
