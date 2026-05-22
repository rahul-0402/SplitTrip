package com.rahulghag.splittrip.feature.profile.model

data class ProfileUiModel(
    val id: String,
    val fullName: String,
    val upiId: String?,
    val currency: String,
    val memberIndex: Int,
    val totalTrips: Int,
    val totalTracked: Double,
)
