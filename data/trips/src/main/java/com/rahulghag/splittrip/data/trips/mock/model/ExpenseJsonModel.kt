package com.rahulghag.splittrip.data.trips.mock.model

import com.rahulghag.splittrip.domain.trips.model.Expense
import com.rahulghag.splittrip.domain.trips.model.ExpenseCategory
import com.rahulghag.splittrip.domain.trips.model.ExpenseSplit
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExpenseSplitJsonModel(
    @SerialName("member_id") val memberId: String,
    @SerialName("member_name") val memberName: String,
    @SerialName("member_index") val memberIndex: Int,
    val amount: Double,
    val percentage: Double,
) {
    fun toModel() = ExpenseSplit(
        memberId = memberId,
        memberName = memberName,
        memberIndex = memberIndex,
        amount = amount,
        percentage = percentage,
    )
}

@Serializable
data class ExpenseJsonModel(
    val id: String,
    @SerialName("trip_id") val tripId: String,
    val title: String,
    val category: String,
    val amount: Double,
    @SerialName("paid_by_name") val paidByName: String,
    @SerialName("paid_by_index") val paidByIndex: Int,
    @SerialName("split_type") val splitType: String,
    @SerialName("your_share") val yourShare: Double,
    val date: String,
    @SerialName("member_count") val memberCount: Int,
    val splits: List<ExpenseSplitJsonModel> = emptyList(),
) {
    fun toModel() = Expense(
        id = id,
        tripId = tripId,
        title = title,
        category = when (category) {
            "FOOD"     -> ExpenseCategory.FOOD
            "STAY"     -> ExpenseCategory.STAY
            "TRAVEL"   -> ExpenseCategory.TRAVEL
            "FUN"      -> ExpenseCategory.FUN
            "SHOPPING" -> ExpenseCategory.SHOPPING
            "FUEL"     -> ExpenseCategory.FUEL
            "MEDICAL"  -> ExpenseCategory.MEDICAL
            else       -> ExpenseCategory.GENERAL
        },
        amount = amount,
        paidByName = paidByName,
        paidByIndex = paidByIndex,
        splitType = splitType,
        yourShare = yourShare,
        date = date,
        memberCount = memberCount,
        splits = splits.map { it.toModel() },
    )
}
