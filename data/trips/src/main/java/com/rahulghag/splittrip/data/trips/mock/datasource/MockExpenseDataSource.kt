@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.rahulghag.splittrip.data.trips.mock.datasource

import com.rahulghag.splittrip.core.common.mock.MockJson
import com.rahulghag.splittrip.data.trips.mock.model.ExpenseJsonModel
import com.rahulghag.splittrip.domain.trips.model.Expense

class MockExpenseDataSource(private val readJson: (String) -> String) {

    fun getExpenses(tripId: String): List<Expense> {
        val json = readJson("expenses.json")
        return MockJson.decodeFromString<List<ExpenseJsonModel>>(json)
            .filter { it.tripId == tripId }
            .map { it.toModel() }
    }

    fun getExpenseById(expenseId: String): Expense? {
        val json = readJson("expenses.json")
        return MockJson.decodeFromString<List<ExpenseJsonModel>>(json)
            .find { it.id == expenseId }
            ?.toModel()
    }
}
