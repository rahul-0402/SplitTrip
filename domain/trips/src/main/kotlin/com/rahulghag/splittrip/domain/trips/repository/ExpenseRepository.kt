package com.rahulghag.splittrip.domain.trips.repository

import com.rahulghag.splittrip.domain.trips.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun getExpenses(tripId: String): Flow<List<Expense>>
    suspend fun getExpenseById(expenseId: String): Expense?
}
