@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.rahulghag.splittrip.data.trips.mock.datasource

import com.rahulghag.splittrip.core.common.mock.MockJson
import com.rahulghag.splittrip.data.trips.mock.model.TripJsonModel
import com.rahulghag.splittrip.domain.trips.model.Trip
import com.rahulghag.splittrip.domain.trips.model.TripMember

class MockTripDataSource(private val readJson: (String) -> String) {

    fun getTrips(): List<Trip> {
        val json = readJson("trips.json")
        return MockJson.decodeFromString<List<TripJsonModel>>(json).map { it.toModel() }
    }

    fun getTripById(tripId: String): Trip? {
        val json = readJson("trips.json")
        return MockJson.decodeFromString<List<TripJsonModel>>(json)
            .find { it.id == tripId }
            ?.toModel()
    }

    fun getTripMembers(tripId: String): List<TripMember> {
        val json = readJson("trips.json")
        return MockJson.decodeFromString<List<TripJsonModel>>(json)
            .find { it.id == tripId }
            ?.members
            ?.map { it.toModel() }
            ?: emptyList()
    }
}
