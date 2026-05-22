@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.rahulghag.splittrip.data.settle.mock.datasource

import com.rahulghag.splittrip.core.common.mock.MockJson
import com.rahulghag.splittrip.data.settle.mock.model.SettlementJsonModel
import com.rahulghag.splittrip.domain.settle.model.Balance
import com.rahulghag.splittrip.domain.settle.model.Settlement
import com.rahulghag.splittrip.domain.trips.model.Member

class MockSettlementDataSource(private val readJson: (String) -> String) {

    fun getBalances(tripId: String): List<Balance> {
        val json = readJson("settlements.json")
        val all = MockJson.decodeFromString<List<SettlementJsonModel>>(json)
        val filtered = all.filter { it.tripId == tripId }
        val currentUser = "Rahul"

        return fakeMembers
            .filter { it.name != currentUser }
            .map { member ->
                val owedToMe = filtered
                    .filter { it.toName == currentUser && it.fromName == member.name }
                    .sumOf { it.amount }
                val iOwe = filtered
                    .filter { it.fromName == currentUser && it.toName == member.name }
                    .sumOf { it.amount }
                Balance(
                    memberId = member.id,
                    memberName = member.name,
                    memberIndex = member.index,
                    netAmount = owedToMe - iOwe,
                )
            }
    }

    fun getSettlements(tripId: String): List<Settlement> {
        val json = readJson("settlements.json")
        return MockJson.decodeFromString<List<SettlementJsonModel>>(json)
            .filter { it.tripId == tripId }
            .map { it.toModel() }
    }

    val fakeMembers = listOf(
        Member("m1", "Rahul", 0, "rahul@upi"),
        Member("m2", "Komal", 1, "komal@upi"),
        Member("m3", "Arun", 2, null),
        Member("m4", "Sara", 3, null),
    )
}
