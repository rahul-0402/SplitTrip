package com.rahulghag.splittrip.data.trips.repository

import com.rahulghag.splittrip.data.trips.mock.datasource.MockExpenseDataSource
import com.rahulghag.splittrip.domain.trips.model.Expense
import com.rahulghag.splittrip.domain.trips.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockExpenseRepositoryImpl(readJson: (String) -> String) : ExpenseRepository {

    private val dataSource = MockExpenseDataSource(readJson)

    override fun getExpenses(tripId: String): Flow<List<Expense>> = flow {
        emit(dataSource.getExpenses(tripId))
    }

    override suspend fun getExpenseById(expenseId: String): Expense? =
        dataSource.getExpenseById(expenseId)
}
