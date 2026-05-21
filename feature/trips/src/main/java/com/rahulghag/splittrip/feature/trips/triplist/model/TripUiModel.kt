package com.rahulghag.splittrip.feature.trips.triplist.model

data class TripUiModel(
    val id: String,
    val name: String,
    val icon: String,
    val memberCount: Int,
    val expenseCount: Int,
    val totalAmount: Double,
    val yourBalance: Double,
    val currency: String = "INR",
    val status: TripStatus,
)

enum class TripStatus { ACTIVE, SETTLING, ARCHIVED }
