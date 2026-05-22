package com.rahulghag.splittrip.domain.trips.model

data class Member(
    val id: String,
    val name: String,
    val index: Int,
    val upiId: String? = null,
)
