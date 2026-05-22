package com.rahulghag.splittrip.feature.trips.repository

import com.rahulghag.splittrip.feature.trips.model.ExpenseUiModel
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun getExpenses(tripId: String): Flow<List<ExpenseUiModel>>
    suspend fun getExpenseById(expenseId: String): ExpenseUiModel?
}
