package com.rahulghag.splittrip.domain.trips.model

data class ExpenseSplit(
    val memberId: String,
    val memberName: String,
    val memberIndex: Int,
    val amount: Double,
    val percentage: Double,
)

data class Expense(
    val id: String,
    val tripId: String,
    val title: String,
    val category: ExpenseCategory,
    val amount: Double,
    val paidByName: String,
    val paidByIndex: Int,
    val splitType: String,
    val yourShare: Double,
    val date: String,
    val memberCount: Int,
    val splits: List<ExpenseSplit> = emptyList(),
)

enum class ExpenseCategory(val emoji: String) {
    FOOD("🍽️"),
    STAY("🏨"),
    TRAVEL("🚗"),
    FUN("🎉"),
    SHOPPING("🛍️"),
    FUEL("⛽"),
    MEDICAL("💊"),
    GENERAL("📝"),
}
