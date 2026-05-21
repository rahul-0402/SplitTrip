package com.rahulghag.splittrip.feature.auth.splash

import com.rahulghag.splittrip.core.ui.viewmodel.SplitTripViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Splash logic:
 * 1. Screen shows entry animations
 * 2. When animations complete → onIntent(AnimationsComplete)
 * 3. ViewModel decides where to navigate
 *    (for now always → Login, auth check added later)
 */
@HiltViewModel
class SplashViewModel @Inject constructor() :
    SplitTripViewModel<SplashState, SplashIntent, SplashEvent>(
        initialState = SplashState()
    ) {

    override fun onIntent(intent: SplashIntent) {
        when (intent) {
            SplashIntent.AnimationsComplete -> {
                updateState { copy(animationsComplete = true) }
                // TODO: check auth state here later
                // For now always navigate to login
                sendEvent(SplashEvent.NavigateToLogin)
            }
        }
    }
}
