package com.rahulghag.splittrip.feature.trips.createtrip

import com.rahulghag.splittrip.core.common.mvi.UiEvent
import com.rahulghag.splittrip.core.common.mvi.UiIntent
import com.rahulghag.splittrip.core.common.mvi.UiState

data class CreateTripState(
    val tripName: String = "",
    val selectedIcon: String = "✈️",
    val startDate: String? = null,
    val endDate: String? = null,
    val isIconPickerOpen: Boolean = false,
    val isStartDatePickerOpen: Boolean = false,
    val isEndDatePickerOpen: Boolean = false,
    val isLoading: Boolean = false,
    val tripNameError: String? = null,
    val dateError: String? = null,
) : UiState

sealed class CreateTripIntent : UiIntent {
    data class TripNameChanged(val name: String) : CreateTripIntent()
    data class IconSelected(val icon: String) : CreateTripIntent()
    data object ToggleIconPicker : CreateTripIntent()
    data class StartDateSelected(val date: String) : CreateTripIntent()
    data class EndDateSelected(val date: String) : CreateTripIntent()
    data object OpenStartDatePicker : CreateTripIntent()
    data object OpenEndDatePicker : CreateTripIntent()
    data object DismissStartDatePicker : CreateTripIntent()
    data object DismissEndDatePicker : CreateTripIntent()
    data object CreateClicked : CreateTripIntent()
    data object DismissClicked : CreateTripIntent()
}

sealed class CreateTripEvent : UiEvent {
    data object Dismiss : CreateTripEvent()
    data class NavigateToTripDetail(val tripId: String) : CreateTripEvent()
    data class ShowSnackbar(val message: String) : CreateTripEvent()
}
