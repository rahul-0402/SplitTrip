package com.rahulghag.splittrip.feature.settle.settleup

import androidx.lifecycle.SavedStateHandle
import com.rahulghag.splittrip.core.ui.viewmodel.SplitTripViewModel
import com.rahulghag.splittrip.feature.settle.repository.SettlementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.catch
import java.net.URLEncoder
import javax.inject.Inject

@HiltViewModel
class SettleUpViewModel @Inject constructor(
    private val settlementRepository: SettlementRepository,
    savedStateHandle: SavedStateHandle,
) : SplitTripViewModel<SettleUpState, SettleUpIntent, SettleUpEvent>(
    initialState = SettleUpState()
) {
    private val tripId: String = checkNotNull(savedStateHandle["tripId"])
    private val tripName: String = savedStateHandle["tripName"] ?: ""

    init {
        updateState { copy(tripId = tripId, tripName = tripName) }
        onIntent(SettleUpIntent.LoadSettlements(tripId))
    }

    override fun onIntent(intent: SettleUpIntent) {
        when (intent) {
            is SettleUpIntent.LoadSettlements,
            is SettleUpIntent.RetryClicked -> loadSettlements(tripId)

            is SettleUpIntent.PayViaUpiClicked -> {
                val s = intent.settlement
                val upiId = s.toUpiId ?: return
                val note = URLEncoder.encode("SplitTrip settlement", "UTF-8")
                val uri = "upi://pay?pa=$upiId&pn=${s.toMemberName}&am=${s.amount}&cu=INR&tn=$note"
                sendEvent(SettleUpEvent.LaunchUpiApp(uri))
            }

            is SettleUpIntent.MarkAsPaidClicked -> {
                launch {
                    settlementRepository.confirmSettlement(intent.settlementId)
                    updateState {
                        copy(confirmedIds = (confirmedIds + intent.settlementId).toImmutableList())
                    }
                    sendEvent(SettleUpEvent.ShowSnackbar("Marked as paid"))
                }
            }
        }
    }

    private fun loadSettlements(tripId: String) {
        launch {
            updateState { copy(isLoading = true, error = null) }
            settlementRepository.getSettlements(tripId)
                .catch { e ->
                    updateState { copy(isLoading = false, error = e.message ?: "Failed to load") }
                }
                .collect { settlements ->
                    updateState {
                        copy(
                            settlements = settlements.toImmutableList(),
                            isLoading = false,
                        )
                    }
                }
        }
    }
}
