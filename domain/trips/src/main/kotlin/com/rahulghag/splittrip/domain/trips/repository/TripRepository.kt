package com.rahulghag.splittrip.domain.trips.repository

import com.rahulghag.splittrip.domain.trips.model.Trip
import com.rahulghag.splittrip.domain.trips.model.TripMember
import kotlinx.coroutines.flow.Flow

interface TripRepository {
    fun getTrips(): Flow<List<Trip>>
    suspend fun getTripById(tripId: String): Trip?
    suspend fun getTripMembers(tripId: String): List<TripMember>
}
