package com.rahulghag.splittrip.data.trips.repository

import com.rahulghag.splittrip.data.trips.mock.datasource.MockTripDataSource
import com.rahulghag.splittrip.domain.trips.model.Trip
import com.rahulghag.splittrip.domain.trips.model.TripMember
import com.rahulghag.splittrip.domain.trips.repository.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockTripRepositoryImpl(readJson: (String) -> String) : TripRepository {

    private val dataSource = MockTripDataSource(readJson)

    override fun getTrips(): Flow<List<Trip>> = flow {
        emit(dataSource.getTrips())
    }

    override suspend fun getTripById(tripId: String): Trip? =
        dataSource.getTripById(tripId)

    override suspend fun getTripMembers(tripId: String): List<TripMember> =
        dataSource.getTripMembers(tripId)
}
