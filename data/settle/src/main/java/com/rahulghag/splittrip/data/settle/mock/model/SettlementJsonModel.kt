package com.rahulghag.splittrip.data.settle.mock.model

import com.rahulghag.splittrip.domain.settle.model.Settlement
import com.rahulghag.splittrip.domain.settle.model.SettlementStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SettlementJsonModel(
    val id: String,
    @SerialName("trip_id") val tripId: String,
    @SerialName("from_name") val fromName: String,
    @SerialName("from_index") val fromIndex: Int,
    @SerialName("to_name") val toName: String,
    @SerialName("to_index") val toIndex: Int,
    @SerialName("to_upi_id") val toUpiId: String?,
    val amount: Double,
    val status: String,
) {
    fun toModel() = Settlement(
        id = id,
        tripId = tripId,
        fromMemberId = "m${fromIndex + 1}",
        fromMemberName = fromName,
        fromMemberIndex = fromIndex,
        toMemberId = "m${toIndex + 1}",
        toMemberName = toName,
        toMemberIndex = toIndex,
        toUpiId = toUpiId,
        amount = amount,
        status = when (status) {
            "CONFIRMED" -> SettlementStatus.CONFIRMED
            "DISPUTED"  -> SettlementStatus.DISPUTED
            else        -> SettlementStatus.PENDING
        },
    )
}
