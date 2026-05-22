package com.rahulghag.splittrip.mock.repository

import com.rahulghag.splittrip.core.common.mock.MockJson
import com.rahulghag.splittrip.feature.settle.mock.SettlementJsonModel
import com.rahulghag.splittrip.feature.settle.model.BalanceUiModel
import com.rahulghag.splittrip.feature.settle.model.SettlementUiModel
import com.rahulghag.splittrip.feature.settle.repository.SettlementRepository
import com.rahulghag.splittrip.feature.trips.mock.FakeMembers
import com.rahulghag.splittrip.mock.AssetReader
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MockSettlementRepository @Inject constructor(
    private val assetReader: AssetReader,
) : SettlementRepository {

    override fun getBalances(tripId: String): Flow<List<BalanceUiModel>> = flow {
        val json = assetReader.read("settlements.json")
        val all = MockJson.decodeFromString<List<SettlementJsonModel>>(json)
        val filtered = all.filter { it.tripId == tripId }
        val currentUser = "Rahul"

        val balances = FakeMembers
            .filter { it.name != currentUser }
            .map { member ->
                val owedToMe = filtered
                    .filter { it.toName == currentUser && it.fromName == member.name }
                    .sumOf { it.amount }
                val iOwe = filtered
                    .filter { it.fromName == currentUser && it.toName == member.name }
                    .sumOf { it.amount }
                BalanceUiModel(
                    memberId = member.id,
                    memberName = member.name,
                    memberIndex = member.index,
                    netAmount = owedToMe - iOwe,
                )
            }
        emit(balances)
    }

    override fun getSettlements(tripId: String): Flow<List<SettlementUiModel>> = flow {
        val json = assetReader.read("settlements.json")
        val all = MockJson.decodeFromString<List<SettlementJsonModel>>(json)
        emit(all.filter { it.tripId == tripId }.map { it.toUiModel() })
    }

    override suspend fun confirmSettlement(settlementId: String) {
        // no-op stub
    }
}
