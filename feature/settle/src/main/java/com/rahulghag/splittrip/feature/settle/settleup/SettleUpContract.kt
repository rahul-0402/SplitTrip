package com.rahulghag.splittrip.feature.settle.settleup

import com.rahulghag.splittrip.core.common.mvi.UiEvent
import com.rahulghag.splittrip.core.common.mvi.UiIntent
import com.rahulghag.splittrip.core.common.mvi.UiState
import com.rahulghag.splittrip.feature.settle.model.SettlementStatus
import com.rahulghag.splittrip.feature.settle.model.SettlementUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class SettleUpState(
    val tripId: String = "",
    val tripName: String = "",
    val settlements: ImmutableList<SettlementUiModel> = persistentListOf(),
    val confirmedIds: ImmutableList<String> = persistentListOf(),
    val isLoading: Boolean = true,
    val error: String? = null,
) : UiState {
    val pendingSettlements: List<SettlementUiModel>
        get() = settlements.filter {
            it.status == SettlementStatus.PENDING && it.id !in confirmedIds
        }
    val confirmedSettlements: List<SettlementUiModel>
        get() = settlements.filter {
            it.status == SettlementStatus.CONFIRMED || it.id in confirmedIds
        }
}

sealed class SettleUpIntent : UiIntent {
    data class LoadSettlements(val tripId: String) : SettleUpIntent()
    data class PayViaUpiClicked(val settlement: SettlementUiModel) : SettleUpIntent()
    data class MarkAsPaidClicked(val settlementId: String) : SettleUpIntent()
    data class RetryClicked(val tripId: String) : SettleUpIntent()
}

sealed class SettleUpEvent : UiEvent {
    data object NavigateUp : SettleUpEvent()
    data class LaunchUpiApp(val upiUri: String) : SettleUpEvent()
    data class ShowSnackbar(val message: String) : SettleUpEvent()
}
