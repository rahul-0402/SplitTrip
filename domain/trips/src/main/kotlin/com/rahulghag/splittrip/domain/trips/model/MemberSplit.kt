package com.rahulghag.splittrip.domain.trips.model

data class MemberSplit(
    val member: Member,
    val amount: Double = 0.0,
    val percentage: Double = 0.0,
    val shares: Int = 1,
    val customAmount: String = "",
    val isIncluded: Boolean = true,
)
