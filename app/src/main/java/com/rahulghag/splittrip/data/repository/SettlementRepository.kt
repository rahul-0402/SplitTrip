package com.rahulghag.splittrip.data.repository

import com.rahulghag.splittrip.feature.settle.model.BalanceUiModel
import com.rahulghag.splittrip.feature.settle.model.SettlementUiModel
import com.rahulghag.splittrip.feature.settle.repository.SettlementRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SettlementRepository @Inject constructor() : SettlementRepository {

    override fun getBalances(tripId: String): Flow<List<BalanceUiModel>> = flow {
        emit(emptyList()) // TODO: Room query
    }

    override fun getSettlements(tripId: String): Flow<List<SettlementUiModel>> = flow {
        emit(emptyList()) // TODO: Room query
    }

    override suspend fun confirmSettlement(settlementId: String) {
        // TODO: Room update
    }
}
