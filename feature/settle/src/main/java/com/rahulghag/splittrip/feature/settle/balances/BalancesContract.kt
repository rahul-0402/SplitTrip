package com.rahulghag.splittrip.feature.settle.balances

import com.rahulghag.splittrip.core.common.mvi.UiEvent
import com.rahulghag.splittrip.core.common.mvi.UiIntent
import com.rahulghag.splittrip.core.common.mvi.UiState
import com.rahulghag.splittrip.domain.settle.model.Balance
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class BalancesState(
    val tripId: String = "",
    val tripName: String = "",
    val balances: ImmutableList<Balance> = persistentListOf(),
    val totalOwed: Double = 0.0,
    val totalOwing: Double = 0.0,
    val isLoading: Boolean = true,
    val error: String? = null,
) : UiState

sealed class BalancesIntent : UiIntent {
    data class LoadBalances(val tripId: String) : BalancesIntent()
    data object SettleUpClicked : BalancesIntent()
    data class RetryClicked(val tripId: String) : BalancesIntent()
}

sealed class BalancesEvent : UiEvent {
    data object NavigateUp : BalancesEvent()
    data class NavigateToSettleUp(val tripId: String, val tripName: String) : BalancesEvent()
}
