package com.rahulghag.splittrip.domain.settle.repository

import com.rahulghag.splittrip.domain.settle.model.Balance
import com.rahulghag.splittrip.domain.settle.model.Settlement
import kotlinx.coroutines.flow.Flow

interface SettlementRepository {
    fun getBalances(tripId: String): Flow<List<Balance>>
    fun getSettlements(tripId: String): Flow<List<Settlement>>
    suspend fun confirmSettlement(settlementId: String)
}
