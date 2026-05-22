package com.rahulghag.splittrip.feature.trips.expensedetail

import com.rahulghag.splittrip.core.common.mvi.UiEvent
import com.rahulghag.splittrip.core.common.mvi.UiIntent
import com.rahulghag.splittrip.core.common.mvi.UiState
import com.rahulghag.splittrip.feature.trips.model.ExpenseUiModel

data class ExpenseDetailState(
    val expense: ExpenseUiModel? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val showDeleteConfirmDialog: Boolean = false,
) : UiState

sealed class ExpenseDetailIntent : UiIntent {
    data class LoadExpense(val expenseId: String) : ExpenseDetailIntent()
    data object EditClicked : ExpenseDetailIntent()
    data object DeleteClicked : ExpenseDetailIntent()
    data object ConfirmDelete : ExpenseDetailIntent()
    data object DismissDeleteDialog : ExpenseDetailIntent()
    data class RetryClicked(val expenseId: String) : ExpenseDetailIntent()
}

sealed class ExpenseDetailEvent : UiEvent {
    data object NavigateUp : ExpenseDetailEvent()
    data class NavigateToEditExpense(val expenseId: String) : ExpenseDetailEvent()
    data class ShowSnackbar(val message: String) : ExpenseDetailEvent()
}
