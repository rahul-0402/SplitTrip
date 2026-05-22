package com.rahulghag.splittrip.feature.trips.repository

import com.rahulghag.splittrip.feature.trips.model.TripMemberUiModel
import com.rahulghag.splittrip.feature.trips.model.TripUiModel
import kotlinx.coroutines.flow.Flow

interface TripRepository {
    fun getTrips(): Flow<List<TripUiModel>>
    suspend fun getTripById(tripId: String): TripUiModel?
    suspend fun getTripMembers(tripId: String): List<TripMemberUiModel>
}
