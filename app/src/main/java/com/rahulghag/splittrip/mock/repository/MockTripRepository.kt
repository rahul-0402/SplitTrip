package com.rahulghag.splittrip.mock.repository

import com.rahulghag.splittrip.core.common.mock.MockJson
import com.rahulghag.splittrip.feature.trips.mock.TripJsonModel
import com.rahulghag.splittrip.feature.trips.model.TripMemberUiModel
import com.rahulghag.splittrip.feature.trips.model.TripUiModel
import com.rahulghag.splittrip.feature.trips.repository.TripRepository
import com.rahulghag.splittrip.mock.AssetReader
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MockTripRepository @Inject constructor(
    private val assetReader: AssetReader,
) : TripRepository {

    override fun getTrips(): Flow<List<TripUiModel>> = flow {
        val json = assetReader.read("trips.json")
        val models = MockJson.decodeFromString<List<TripJsonModel>>(json)
        emit(models.map { it.toUiModel() })
    }

    override suspend fun getTripById(tripId: String): TripUiModel? {
        val json = assetReader.read("trips.json")
        val models = MockJson.decodeFromString<List<TripJsonModel>>(json)
        return models.find { it.id == tripId }?.toUiModel()
    }

    override suspend fun getTripMembers(tripId: String): List<TripMemberUiModel> {
        val json = assetReader.read("trips.json")
        val models = MockJson.decodeFromString<List<TripJsonModel>>(json)
        return models.find { it.id == tripId }?.members?.map { it.toUiModel() } ?: emptyList()
    }
}
