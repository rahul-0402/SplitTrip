package com.rahulghag.splittrip.test

import com.rahulghag.splittrip.core.common.mvi.UiEvent
import com.rahulghag.splittrip.core.common.mvi.UiIntent
import com.rahulghag.splittrip.core.common.mvi.UiState

// Simple counter to verify MVI pattern works

data class CounterState(
    val count: Int = 0,
    val isLoading: Boolean = false,
) : UiState

sealed class CounterIntent : UiIntent {
    data object Increment : CounterIntent()
    data object Decrement : CounterIntent()
    data object Reset : CounterIntent()
}

sealed class CounterEvent : UiEvent {
    data object ShowResetMessage : CounterEvent()
}
