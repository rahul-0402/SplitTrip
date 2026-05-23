package com.rahulghag.splittrip.data.profile.mock.model

import com.rahulghag.splittrip.domain.profile.model.Profile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileJsonModel(
    val id: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("upi_id") val upiId: String? = null,
    val currency: String = "INR",
    @SerialName("member_index") val memberIndex: Int = 0,
    @SerialName("total_trips") val totalTrips: Int = 0,
    @SerialName("total_tracked") val totalTracked: Double = 0.0,
) {
    fun toModel() = Profile(
        id = id,
        fullName = fullName,
        upiId = upiId,
        currency = currency,
        memberIndex = memberIndex,
        totalTrips = totalTrips,
        totalTracked = totalTracked,
    )
}
