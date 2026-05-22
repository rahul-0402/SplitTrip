package com.rahulghag.splittrip.feature.settle.balances

import androidx.lifecycle.SavedStateHandle
import com.rahulghag.splittrip.core.ui.viewmodel.SplitTripViewModel
import com.rahulghag.splittrip.domain.settle.repository.SettlementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class BalancesViewModel @Inject constructor(
    private val settlementRepository: SettlementRepository,
    savedStateHandle: SavedStateHandle,
) : SplitTripViewModel<BalancesState, BalancesIntent, BalancesEvent>(
    initialState = BalancesState()
) {
    private val tripId: String = checkNotNull(savedStateHandle["tripId"])
    private val tripName: String = savedStateHandle["tripName"] ?: ""

    init {
        updateState { copy(tripId = tripId, tripName = tripName) }
        onIntent(BalancesIntent.LoadBalances(tripId))
    }

    override fun onIntent(intent: BalancesIntent) {
        when (intent) {
            is BalancesIntent.LoadBalances,
            is BalancesIntent.RetryClicked -> loadBalances(tripId)

            BalancesIntent.SettleUpClicked ->
                sendEvent(BalancesEvent.NavigateToSettleUp(tripId, tripName))
        }
    }

    private fun loadBalances(tripId: String) {
        launch {
            updateState { copy(isLoading = true, error = null) }
            settlementRepository.getBalances(tripId)
                .catch { e ->
                    updateState { copy(isLoading = false, error = e.message ?: "Failed to load balances") }
                }
                .collect { balances ->
                    updateState {
                        copy(
                            balances = balances.toImmutableList(),
                            totalOwed = balances.filter { it.netAmount > 0 }.sumOf { it.netAmount },
                            totalOwing = balances.filter { it.netAmount < 0 }.sumOf { -it.netAmount },
                            isLoading = false,
                        )
                    }
                }
        }
    }
}
