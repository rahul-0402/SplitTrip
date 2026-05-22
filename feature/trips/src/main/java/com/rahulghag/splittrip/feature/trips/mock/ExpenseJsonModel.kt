package com.rahulghag.splittrip.feature.trips.mock

import com.rahulghag.splittrip.feature.trips.model.ExpenseCategory
import com.rahulghag.splittrip.feature.trips.model.ExpenseSplitUiModel
import com.rahulghag.splittrip.feature.trips.model.ExpenseUiModel
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
    fun toUiModel() = ExpenseSplitUiModel(
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
    fun toUiModel() = ExpenseUiModel(
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
        splits = splits.map { it.toUiModel() },
    )
}
