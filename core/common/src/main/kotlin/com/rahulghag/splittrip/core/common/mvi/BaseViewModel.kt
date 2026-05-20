package com.rahulghag.splittrip.core.common.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Pure Kotlin MVI base — no Jetpack dependency.
 * KMP-ready: works on Android, iOS, Desktop.
 *
 * Each screen has exactly 3 things:
 *   State  — everything the UI needs to render (StateFlow)
 *   Intent — every action the user can take
 *   Event  — one-time side effects (navigate, show snackbar)
 *
 * Why StateFlow for state?
 *   Survives recomposition. UI always has latest value on subscribe.
 *
 * Why Channel for events?
 *   Events are consumed exactly once.
 *   Channel buffers if collector is slow.
 *   Prevents duplicate navigation on recomposition.
 *
 * @param State  — data class implementing UiState
 * @param Intent — sealed class implementing UiIntent
 * @param Event  — sealed class implementing UiEvent
 */
abstract class BaseViewModel<State : UiState, Intent : UiIntent, Event : UiEvent>(
    initialState: State,
) {
    // Backing state — mutable internally, immutable externally
    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    // One-shot events — Channel with BUFFERED capacity
    // so events sent before UI subscribes are not dropped
    private val _uiEvent = Channel<Event>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    // Current state shorthand
    protected val currentState: State get() = _uiState.value

    /**
     * Every user action comes through here.
     * Subclasses handle each Intent and call
     * updateState() or sendEvent() accordingly.
     */
    abstract fun onIntent(intent: Intent)

    /**
     * Update state immutably.
     * Usage: updateState { copy(isLoading = true) }
     */
    protected fun updateState(block: State.() -> State) {
        _uiState.update { it.block() }
    }

    /**
     * Send a one-time event to the UI.
     * Usage: sendEvent(scope) { MyEvent.NavigateToHome }
     */
    protected fun sendEvent(scope: CoroutineScope, event: Event) {
        scope.launch { _uiEvent.send(event) }
    }
}
