package com.rahulghag.splittrip.feature.trips.triplist.model

object FakeTripsData {
    val trips = listOf(
        TripUiModel(
            id = "trip_1",
            name = "Goa trip 2025",
            icon = "✈️",
            memberCount = 4,
            expenseCount = 12,
            totalAmount = 18420.0,
            yourBalance = 840.0,
            status = TripStatus.ACTIVE,
        ),
        TripUiModel(
            id = "trip_2",
            name = "Manali weekend",
            icon = "🏔️",
            memberCount = 3,
            expenseCount = 8,
            totalAmount = 12600.0,
            yourBalance = -1200.0,
            status = TripStatus.ACTIVE,
        ),
        TripUiModel(
            id = "trip_3",
            name = "Bangkok 2024",
            icon = "🌏",
            memberCount = 5,
            expenseCount = 24,
            totalAmount = 45800.0,
            yourBalance = 0.0,
            status = TripStatus.ARCHIVED,
        ),
        TripUiModel(
            id = "trip_4",
            name = "Coorg camping",
            icon = "🏕️",
            memberCount = 6,
            expenseCount = 3,
            totalAmount = 4200.0,
            yourBalance = -320.0,
            status = TripStatus.ACTIVE,
        ),
    )
}
