package com.rahulghag.splittrip.data.trips.mock.model

import com.rahulghag.splittrip.domain.trips.model.MemberRole
import com.rahulghag.splittrip.domain.trips.model.Trip
import com.rahulghag.splittrip.domain.trips.model.TripMember
import com.rahulghag.splittrip.domain.trips.model.TripStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TripMemberJsonModel(
    val id: String,
    val name: String,
    @SerialName("member_index") val memberIndex: Int,
    @SerialName("upi_id") val upiId: String? = null,
    val role: String = "MEMBER",
    @SerialName("joined_at") val joinedAt: String,
) {
    fun toModel() = TripMember(
        id = id,
        name = name,
        memberIndex = memberIndex,
        upiId = upiId,
        role = if (role == "ADMIN") MemberRole.ADMIN else MemberRole.MEMBER,
        joinedAt = joinedAt,
    )
}

@Serializable
data class TripJsonModel(
    val id: String,
    val name: String,
    val icon: String,
    @SerialName("member_count") val memberCount: Int,
    @SerialName("expense_count") val expenseCount: Int,
    @SerialName("total_amount") val totalAmount: Double,
    @SerialName("your_balance") val yourBalance: Double,
    val currency: String = "INR",
    val status: String = "ACTIVE",
    @SerialName("start_date") val startDate: String? = null,
    @SerialName("end_date") val endDate: String? = null,
    @SerialName("invite_code") val inviteCode: String = "",
    val members: List<TripMemberJsonModel> = emptyList(),
) {
    fun toModel() = Trip(
        id = id,
        name = name,
        icon = icon,
        memberCount = memberCount,
        expenseCount = expenseCount,
        totalAmount = totalAmount,
        yourBalance = yourBalance,
        currency = currency,
        status = when (status) {
            "ARCHIVED" -> TripStatus.ARCHIVED
            "SETTLING" -> TripStatus.SETTLING
            else -> TripStatus.ACTIVE
        },
        members = members.map { it.toModel() },
        inviteCode = inviteCode,
    )
}
