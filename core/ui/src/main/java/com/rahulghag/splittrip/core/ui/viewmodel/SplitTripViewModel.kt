package com.rahulghag.splittrip.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahulghag.splittrip.core.common.mvi.BaseViewModel
import com.rahulghag.splittrip.core.common.mvi.UiEvent
import com.rahulghag.splittrip.core.common.mvi.UiIntent
import com.rahulghag.splittrip.core.common.mvi.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Bridges pure Kotlin BaseViewModel with Jetpack ViewModel.
 *
 * Why this separation?
 *   - BaseViewModel (core:common) — pure Kotlin, KMP-ready
 *   - SplitTripViewModel (core:ui) — Android lifecycle aware
 *
 * Feature ViewModels extend SplitTripViewModel, NOT BaseViewModel.
 * This gives them:
 *   - viewModelScope for coroutine cancellation on clear
 *   - Hilt injection support
 *   - Jetpack SavedStateHandle access if needed
 *   - Survive configuration changes
 */
abstract class SplitTripViewModel<State : UiState, Intent : UiIntent, Event : UiEvent>(
    initialState: State,
) : ViewModel() {

    // Delegate to pure Kotlin base.
    // Wrapper methods expose the protected BaseViewModel API to this class,
    // since Kotlin's 'protected' is not accessible across class boundaries
    // even when holding a reference to the subclass instance.
    private val delegate = object : BaseViewModel<State, Intent, Event>(initialState) {
        override fun onIntent(intent: Intent) {
            this@SplitTripViewModel.onIntent(intent)
        }

        fun updateStateInternal(block: State.() -> State) = updateState(block)
        fun sendEventInternal(scope: CoroutineScope, event: Event) = sendEvent(scope, event)
    }

    val uiState: StateFlow<State> = delegate.uiState
    val uiEvent = delegate.uiEvent

    protected val currentState: State get() = delegate.uiState.value

    abstract fun onIntent(intent: Intent)

    protected fun updateState(block: State.() -> State) {
        delegate.updateStateInternal(block)
    }

    protected fun sendEvent(event: Event) {
        delegate.sendEventInternal(viewModelScope, event)
    }

    /**
     * Launch a coroutine with automatic cancellation on ViewModel clear.
     * Usage: launch { repository.getTrips() }
     */
    protected fun launch(
        block: suspend CoroutineScope.() -> Unit,
    ) = viewModelScope.launch(block = block)
}
