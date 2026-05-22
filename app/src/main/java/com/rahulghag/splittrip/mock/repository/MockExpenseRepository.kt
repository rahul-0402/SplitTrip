package com.rahulghag.splittrip.mock.repository

import com.rahulghag.splittrip.core.common.mock.MockJson
import com.rahulghag.splittrip.feature.trips.mock.ExpenseJsonModel
import com.rahulghag.splittrip.feature.trips.model.ExpenseUiModel
import com.rahulghag.splittrip.feature.trips.repository.ExpenseRepository
import com.rahulghag.splittrip.mock.AssetReader
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MockExpenseRepository @Inject constructor(
    private val assetReader: AssetReader,
) : ExpenseRepository {

    override fun getExpenses(tripId: String): Flow<List<ExpenseUiModel>> = flow {
        val json = assetReader.read("expenses.json")
        val all = MockJson.decodeFromString<List<ExpenseJsonModel>>(json)
        emit(all.filter { it.tripId == tripId }.map { it.toUiModel() })
    }

    override suspend fun getExpenseById(expenseId: String): ExpenseUiModel? {
        val json = assetReader.read("expenses.json")
        val all = MockJson.decodeFromString<List<ExpenseJsonModel>>(json)
        return all.find { it.id == expenseId }?.toUiModel()
    }
}
