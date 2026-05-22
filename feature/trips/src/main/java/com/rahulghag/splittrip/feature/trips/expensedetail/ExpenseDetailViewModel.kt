package com.rahulghag.splittrip.feature.trips.expensedetail

import androidx.lifecycle.SavedStateHandle
import com.rahulghag.splittrip.core.ui.viewmodel.SplitTripViewModel
import com.rahulghag.splittrip.feature.trips.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class ExpenseDetailViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    savedStateHandle: SavedStateHandle,
) : SplitTripViewModel<ExpenseDetailState, ExpenseDetailIntent, ExpenseDetailEvent>(
    initialState = ExpenseDetailState()
) {
    private val expenseId: String = checkNotNull(savedStateHandle["expenseId"])

    init {
        onIntent(ExpenseDetailIntent.LoadExpense(expenseId))
    }

    override fun onIntent(intent: ExpenseDetailIntent) {
        when (intent) {
            is ExpenseDetailIntent.LoadExpense,
            is ExpenseDetailIntent.RetryClicked -> loadExpense(expenseId)

            ExpenseDetailIntent.EditClicked ->
                sendEvent(ExpenseDetailEvent.NavigateToEditExpense(expenseId))

            ExpenseDetailIntent.DeleteClicked ->
                updateState { copy(showDeleteConfirmDialog = true) }

            ExpenseDetailIntent.ConfirmDelete -> confirmDelete()

            ExpenseDetailIntent.DismissDeleteDialog ->
                updateState { copy(showDeleteConfirmDialog = false) }
        }
    }

    private fun loadExpense(expenseId: String) {
        launch {
            updateState { copy(isLoading = true) }
            val expense = expenseRepository.getExpenseById(expenseId)
            updateState { copy(expense = expense, isLoading = false) }
        }
    }

    private fun confirmDelete() {
        updateState { copy(showDeleteConfirmDialog = false) }
        launch {
            updateState { copy(isLoading = true) }
            delay(600)
            sendEvent(ExpenseDetailEvent.NavigateUp)
            sendEvent(ExpenseDetailEvent.ShowSnackbar("Expense deleted"))
        }
    }
}
