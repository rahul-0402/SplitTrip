package com.rahulghag.splittrip.feature.auth.forgotpassword

import com.rahulghag.splittrip.core.common.mvi.UiEvent
import com.rahulghag.splittrip.core.common.mvi.UiIntent
import com.rahulghag.splittrip.core.common.mvi.UiState

data class ForgotPasswordState(
    val email: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val generalError: String? = null,
    val successMessage: String? = null,
) : UiState

sealed class ForgotPasswordIntent : UiIntent {
    data class EmailChanged(val email: String) : ForgotPasswordIntent()
    data object SendResetEmailClicked : ForgotPasswordIntent()
    data object NavigateUpClicked : ForgotPasswordIntent()
    data object ClearGeneralError : ForgotPasswordIntent()
}

sealed class ForgotPasswordEvent : UiEvent {
    data object NavigateUp : ForgotPasswordEvent()
}
