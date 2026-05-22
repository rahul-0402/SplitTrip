package com.rahulghag.splittrip.domain.profile.model

data class Profile(
    val id: String,
    val fullName: String,
    val upiId: String?,
    val currency: String,
    val memberIndex: Int,
    val totalTrips: Int,
    val totalTracked: Double,
)
