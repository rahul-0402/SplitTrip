package com.rahulghag.splittrip.feature.trips.tripdetail

import com.rahulghag.splittrip.core.common.mvi.UiEvent
import com.rahulghag.splittrip.core.common.mvi.UiIntent
import com.rahulghag.splittrip.core.common.mvi.UiState
import com.rahulghag.splittrip.feature.trips.model.ExpenseCategory
import com.rahulghag.splittrip.feature.trips.model.ExpenseUiModel
import com.rahulghag.splittrip.feature.trips.model.TripUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class TripDetailState(
    val trip: TripUiModel? = null,
    val expenses: ImmutableList<ExpenseUiModel> = persistentListOf(),
    val filteredExpenses: ImmutableList<ExpenseUiModel> = persistentListOf(),
    val selectedCategory: ExpenseCategory? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
) : UiState {
    val totalExpenses: Int get() = expenses.size
    val displayExpenses: ImmutableList<ExpenseUiModel>
        get() = if (selectedCategory == null) expenses else filteredExpenses
}

sealed class TripDetailIntent : UiIntent {
    data class LoadTripDetail(val tripId: String) : TripDetailIntent()
    data class CategoryFilterSelected(val category: ExpenseCategory?) : TripDetailIntent()
    data class ExpenseClicked(val expenseId: String) : TripDetailIntent()
    data object AddExpenseClicked : TripDetailIntent()
    data object BalancesClicked : TripDetailIntent()
    data object ManageMembersClicked : TripDetailIntent()
    data class RetryClicked(val tripId: String) : TripDetailIntent()
}

sealed class TripDetailEvent : UiEvent {
    data class NavigateToExpenseDetail(val expenseId: String) : TripDetailEvent()
    data class NavigateToAddExpense(val tripId: String) : TripDetailEvent()
    data class NavigateToBalances(val tripId: String, val tripName: String) : TripDetailEvent()
    data class NavigateToTripMembers(val tripId: String, val tripName: String) : TripDetailEvent()
    data object NavigateUp : TripDetailEvent()
}
