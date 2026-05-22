package com.rahulghag.splittrip.feature.settle.repository

import com.rahulghag.splittrip.feature.settle.model.BalanceUiModel
import com.rahulghag.splittrip.feature.settle.model.SettlementUiModel
import kotlinx.coroutines.flow.Flow

interface SettlementRepository {
    fun getBalances(tripId: String): Flow<List<BalanceUiModel>>
    fun getSettlements(tripId: String): Flow<List<SettlementUiModel>>
    suspend fun confirmSettlement(settlementId: String)
}
