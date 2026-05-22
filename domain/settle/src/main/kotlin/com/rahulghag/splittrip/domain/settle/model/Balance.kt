package com.rahulghag.splittrip.domain.settle.model

data class Balance(
    val memberId: String,
    val memberName: String,
    val memberIndex: Int,
    val netAmount: Double,
    // positive = owed to you, negative = you owe them, 0 = settled
)
