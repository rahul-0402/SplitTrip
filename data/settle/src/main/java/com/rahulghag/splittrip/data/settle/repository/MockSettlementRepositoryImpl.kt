package com.rahulghag.splittrip.data.settle.repository

import com.rahulghag.splittrip.data.settle.mock.datasource.MockSettlementDataSource
import com.rahulghag.splittrip.domain.settle.model.Balance
import com.rahulghag.splittrip.domain.settle.model.Settlement
import com.rahulghag.splittrip.domain.settle.repository.SettlementRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockSettlementRepositoryImpl(readJson: (String) -> String) : SettlementRepository {

    private val dataSource = MockSettlementDataSource(readJson)

    override fun getBalances(tripId: String): Flow<List<Balance>> = flow {
        emit(dataSource.getBalances(tripId))
    }

    override fun getSettlements(tripId: String): Flow<List<Settlement>> = flow {
        emit(dataSource.getSettlements(tripId))
    }

    override suspend fun confirmSettlement(settlementId: String) = Unit
}
