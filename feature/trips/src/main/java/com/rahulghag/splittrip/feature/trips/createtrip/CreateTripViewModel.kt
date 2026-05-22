package com.rahulghag.splittrip.feature.trips.createtrip

import com.rahulghag.splittrip.core.ui.viewmodel.SplitTripViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class CreateTripViewModel @Inject constructor() :
    SplitTripViewModel<CreateTripState, CreateTripIntent, CreateTripEvent>(
        initialState = CreateTripState()
    ) {

    val availableIcons = listOf(
        "✈️", "🏔️", "🏖️", "🏕️", "🌏",
        "🚂", "🚢", "🏍️", "🎒", "🗺️",
        "🌴", "🏙️", "🎡", "⛷️", "🤿",
    )

    override fun onIntent(intent: CreateTripIntent) {
        when (intent) {
            is CreateTripIntent.TripNameChanged ->
                updateState { copy(tripName = intent.name, tripNameError = null) }

            is CreateTripIntent.IconSelected ->
                updateState { copy(selectedIcon = intent.icon, isIconPickerOpen = false) }

            CreateTripIntent.ToggleIconPicker ->
                updateState { copy(isIconPickerOpen = !isIconPickerOpen) }

            is CreateTripIntent.StartDateSelected -> {
                updateState { copy(startDate = intent.date, isStartDatePickerOpen = false) }
                validateDates()
            }

            is CreateTripIntent.EndDateSelected -> {
                updateState { copy(endDate = intent.date, isEndDatePickerOpen = false) }
                validateDates()
            }

            CreateTripIntent.OpenStartDatePicker ->
                updateState { copy(isStartDatePickerOpen = true) }

            CreateTripIntent.OpenEndDatePicker ->
                updateState { copy(isEndDatePickerOpen = true) }

            CreateTripIntent.DismissStartDatePicker ->
                updateState { copy(isStartDatePickerOpen = false) }

            CreateTripIntent.DismissEndDatePicker ->
                updateState { copy(isEndDatePickerOpen = false) }

            CreateTripIntent.CreateClicked -> create()

            CreateTripIntent.DismissClicked ->
                sendEvent(CreateTripEvent.Dismiss)
        }
    }

    private fun validateDates() {
        val start = currentState.startDate ?: return
        val end = currentState.endDate ?: return
        updateState {
            copy(dateError = if (end < start) "End date must be after start date" else null)
        }
    }

    private fun create() {
        val state = currentState
        val nameError = when {
            state.tripName.isBlank() -> "Trip name is required"
            state.tripName.length < 2 -> "Trip name is too short"
            else -> null
        }
        val dateError = if (state.startDate != null && state.endDate != null
            && state.endDate < state.startDate) {
            "End date must be after start date"
        } else null

        if (nameError != null || dateError != null) {
            updateState { copy(tripNameError = nameError, dateError = dateError) }
            return
        }

        updateState { copy(isLoading = true) }
        launch {
            delay(800)
            val fakeId = "trip_${System.currentTimeMillis()}"
            sendEvent(CreateTripEvent.NavigateToTripDetail(fakeId))
        }
    }
}
