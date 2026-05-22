package com.rahulghag.splittrip.feature.trips.tripdetail

import androidx.lifecycle.SavedStateHandle
import com.rahulghag.splittrip.core.ui.viewmodel.SplitTripViewModel
import com.rahulghag.splittrip.feature.trips.model.ExpenseCategory
import com.rahulghag.splittrip.feature.trips.repository.ExpenseRepository
import com.rahulghag.splittrip.feature.trips.repository.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class TripDetailViewModel @Inject constructor(
    private val tripRepository: TripRepository,
    private val expenseRepository: ExpenseRepository,
    savedStateHandle: SavedStateHandle,
) : SplitTripViewModel<TripDetailState, TripDetailIntent, TripDetailEvent>(
    initialState = TripDetailState()
) {
    private val tripId: String = checkNotNull(savedStateHandle["tripId"])

    init {
        onIntent(TripDetailIntent.LoadTripDetail(tripId))
    }

    override fun onIntent(intent: TripDetailIntent) {
        when (intent) {
            is TripDetailIntent.LoadTripDetail,
            is TripDetailIntent.RetryClicked -> loadTripDetail(tripId)

            is TripDetailIntent.CategoryFilterSelected -> updateFilter(intent.category)

            is TripDetailIntent.ExpenseClicked ->
                sendEvent(TripDetailEvent.NavigateToExpenseDetail(intent.expenseId))

            TripDetailIntent.AddExpenseClicked ->
                sendEvent(TripDetailEvent.NavigateToAddExpense(tripId))

            TripDetailIntent.BalancesClicked ->
                sendEvent(TripDetailEvent.NavigateToBalances(tripId, currentState.trip?.name ?: ""))

            TripDetailIntent.ManageMembersClicked ->
                sendEvent(TripDetailEvent.NavigateToTripMembers(tripId, currentState.trip?.name ?: ""))
        }
    }

    private fun loadTripDetail(tripId: String) {
        updateState { copy(isLoading = true, error = null) }
        launch {
            val trip = tripRepository.getTripById(tripId)
            updateState { copy(trip = trip) }
        }
        launch {
            expenseRepository.getExpenses(tripId)
                .catch { e ->
                    updateState {
                        copy(
                            isLoading = false,
                            error = e.message ?: "Failed to load expenses",
                        )
                    }
                }
                .collect { expenses ->
                    updateState {
                        copy(
                            expenses = expenses.toImmutableList(),
                            isLoading = false,
                        )
                    }
                }
        }
    }

    private fun updateFilter(category: ExpenseCategory?) {
        updateState {
            copy(
                selectedCategory = category,
                filteredExpenses = if (category == null)
                    expenses
                else
                    expenses.filter { it.category == category }.toImmutableList(),
            )
        }
    }
}
