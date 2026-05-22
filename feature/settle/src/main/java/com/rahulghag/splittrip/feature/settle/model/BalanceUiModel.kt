package com.rahulghag.splittrip.feature.settle.model

data class BalanceUiModel(
    val memberId: String,
    val memberName: String,
    val memberIndex: Int,
    val netAmount: Double,
    // positive = owed to you, negative = you owe them, 0 = settled
)
