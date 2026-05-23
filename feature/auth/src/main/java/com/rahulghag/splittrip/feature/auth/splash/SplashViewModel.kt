package com.rahulghag.splittrip.feature.auth.splash

import com.rahulghag.splittrip.domain.auth.repository.SessionRepository
import com.rahulghag.splittrip.core.ui.viewmodel.SplitTripViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
) : SplitTripViewModel<SplashState, SplashIntent, SplashEvent>(
    initialState = SplashState()
) {

    override fun onIntent(intent: SplashIntent) {
        when (intent) {
            SplashIntent.AnimationsComplete -> {
                updateState { copy(animationsComplete = true) }
                val destination = if (sessionRepository.isLoggedIn())
                    SplashEvent.NavigateToTripList
                else
                    SplashEvent.NavigateToLogin
                sendEvent(destination)
            }
        }
    }
}
