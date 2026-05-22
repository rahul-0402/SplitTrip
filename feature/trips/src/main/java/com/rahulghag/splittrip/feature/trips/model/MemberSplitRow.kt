package com.rahulghag.splittrip.feature.trips.model

data class MemberSplitRow(
    val member: MemberUiModel,
    val amount: Double = 0.0,
    val percentage: Double = 0.0,
    val shares: Int = 1,
    val customAmount: String = "",
    val isIncluded: Boolean = true,
)
