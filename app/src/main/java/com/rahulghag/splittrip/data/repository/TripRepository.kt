package com.rahulghag.splittrip.data.repository

import com.rahulghag.splittrip.feature.trips.model.TripMemberUiModel
import com.rahulghag.splittrip.feature.trips.model.TripUiModel
import com.rahulghag.splittrip.feature.trips.repository.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TripRepository @Inject constructor() : TripRepository {

    override fun getTrips(): Flow<List<TripUiModel>> = flow {
        emit(emptyList())
    }

    override suspend fun getTripById(tripId: String): TripUiModel? = null

    override suspend fun getTripMembers(tripId: String): List<TripMemberUiModel> =
        emptyList() // TODO: Room query
}
