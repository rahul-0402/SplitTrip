package com.rahulghag.splittrip.domain.trips.usecase

import com.rahulghag.splittrip.domain.trips.model.MemberSplit
import com.rahulghag.splittrip.domain.trips.model.SplitType
import javax.inject.Inject
import kotlin.math.floor
import kotlin.math.roundToInt

class CalculateSplitsUseCase @Inject constructor() {

    operator fun invoke(
        splits: List<MemberSplit>,
        total: Double,
        splitType: SplitType,
    ): List<MemberSplit> {
        val included = splits.filter { it.isIncluded }
        val count = included.size

        if (count == 0) return splits.map { it.copy(amount = 0.0) }

        return when (splitType) {
            SplitType.EQUAL -> {
                val base = floor(total / count * 100) / 100.0
                val remainder = ((total - base * (count - 1)) * 100).roundToInt() / 100.0
                val lastId = included.last().member.id
                splits.map { row ->
                    when {
                        !row.isIncluded -> row.copy(amount = 0.0)
                        row.member.id == lastId -> row.copy(amount = remainder)
                        else -> row.copy(amount = base)
                    }
                }
            }

            SplitType.PERCENTAGE -> splits.map { row ->
                row.copy(
                    amount = if (row.isIncluded)
                        (total * row.percentage / 100.0 * 100).roundToInt() / 100.0
                    else 0.0
                )
            }

            SplitType.CUSTOM -> splits.map { row ->
                row.copy(amount = if (row.isIncluded) row.customAmount.toDoubleOrNull() ?: 0.0 else 0.0)
            }

            SplitType.SHARES -> {
                val totalShares = included.sumOf { it.shares }.toDouble()
                if (totalShares == 0.0) return splits
                val amounts = mutableMapOf<String, Double>()
                var sum = 0.0
                included.dropLast(1).forEach { row ->
                    val share = floor(total * row.shares / totalShares * 100) / 100.0
                    amounts[row.member.id] = share
                    sum += share
                }
                included.lastOrNull()?.let { last ->
                    amounts[last.member.id] = ((total - sum) * 100).roundToInt() / 100.0
                }
                splits.map { row -> row.copy(amount = amounts[row.member.id] ?: 0.0) }
            }
        }
    }
}
