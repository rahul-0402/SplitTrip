package com.rahulghag.splittrip.domain.trips.usecase

import com.rahulghag.splittrip.domain.trips.model.MemberSplit
import com.rahulghag.splittrip.domain.trips.model.SplitType
import javax.inject.Inject
import kotlin.math.abs

class ValidateExpenseUseCase @Inject constructor() {

    operator fun invoke(
        splits: List<MemberSplit>,
        total: Double,
        splitType: SplitType,
    ): String? {
        val included = splits.filter { it.isIncluded }
        if (included.isEmpty()) return "At least one member must be included"
        return when (splitType) {
            SplitType.EQUAL -> null
            SplitType.PERCENTAGE -> {
                val sum = included.sumOf { it.percentage }
                if (abs(sum - 100.0) > 0.01) "Percentages must add up to 100%" else null
            }
            SplitType.CUSTOM -> {
                val sum = included.sumOf { it.customAmount.toDoubleOrNull() ?: 0.0 }
                val label = if (total % 1.0 == 0.0) total.toInt().toString() else "%.2f".format(total)
                if (abs(sum - total) > 0.01) "Amounts must add up to ₹$label" else null
            }
            SplitType.SHARES -> null
        }
    }
}
