package com.rahulghag.splittrip.feature.trips.model

data class MemberUiModel(
    val id: String,
    val name: String,
    val index: Int,
    val upiId: String? = null,
)
