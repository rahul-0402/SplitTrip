package com.rahulghag.splittrip.feature.auth.splash

import com.rahulghag.splittrip.core.common.mvi.UiEvent
import com.rahulghag.splittrip.core.common.mvi.UiIntent
import com.rahulghag.splittrip.core.common.mvi.UiState

data class SplashState(
    // Whether the entry animations have completed
    val animationsComplete: Boolean = false,
) : UiState

sealed class SplashIntent : UiIntent {
    // Fired when all animations finish
    data object AnimationsComplete : SplashIntent()
}

sealed class SplashEvent : UiEvent {
    // Navigate to login — user not authenticated
    data object NavigateToLogin : SplashEvent()
    // Navigate to trip list — user already authenticated
    data object NavigateToTripList : SplashEvent()
}
