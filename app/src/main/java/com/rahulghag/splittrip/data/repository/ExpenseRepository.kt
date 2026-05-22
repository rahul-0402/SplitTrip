package com.rahulghag.splittrip.data.repository

import com.rahulghag.splittrip.feature.trips.model.ExpenseUiModel
import com.rahulghag.splittrip.feature.trips.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ExpenseRepository @Inject constructor() : ExpenseRepository {

    override fun getExpenses(tripId: String): Flow<List<ExpenseUiModel>> = flow {
        emit(emptyList()) // TODO: Room query
    }

    override suspend fun getExpenseById(expenseId: String): ExpenseUiModel? = null // TODO: Room
}
