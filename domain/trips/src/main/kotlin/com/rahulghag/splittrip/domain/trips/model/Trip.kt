package com.rahulghag.splittrip.domain.trips.model

data class TripMember(
    val id: String,
    val name: String,
    val memberIndex: Int,
    val upiId: String?,
    val role: MemberRole,
    val joinedAt: String,
)

enum class MemberRole { ADMIN, MEMBER }

data class Trip(
    val id: String,
    val name: String,
    val icon: String,
    val memberCount: Int,
    val expenseCount: Int,
    val totalAmount: Double,
    val yourBalance: Double,
    val currency: String = "INR",
    val status: TripStatus,
    val members: List<TripMember> = emptyList(),
    val inviteCode: String = "",
)

enum class TripStatus { ACTIVE, SETTLING, ARCHIVED }
