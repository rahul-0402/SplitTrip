package com.rahulghag.splittrip.feature.settle.model

data class SettlementUiModel(
    val id: String,
    val tripId: String,
    val fromMemberId: String,
    val fromMemberName: String,
    val fromMemberIndex: Int,
    val toMemberId: String,
    val toMemberName: String,
    val toMemberIndex: Int,
    val toUpiId: String?,
    val amount: Double,
    val status: SettlementStatus,
)

enum class SettlementStatus { PENDING, CONFIRMED, DISPUTED }
