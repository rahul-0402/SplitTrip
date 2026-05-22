package com.rahulghag.splittrip.feature.trips.addexpense

import com.rahulghag.splittrip.core.common.mvi.UiEvent
import com.rahulghag.splittrip.core.common.mvi.UiIntent
import com.rahulghag.splittrip.core.common.mvi.UiState
import com.rahulghag.splittrip.feature.trips.model.MemberSplitRow
import com.rahulghag.splittrip.feature.trips.model.MemberUiModel
import com.rahulghag.splittrip.feature.trips.model.SplitType
import com.rahulghag.splittrip.feature.trips.model.ExpenseCategory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class AddExpenseState(
    val tripId: String = "",
    val amount: String = "",
    val description: String = "",
    val selectedCategory: ExpenseCategory = ExpenseCategory.GENERAL,
    val paidBy: MemberUiModel? = null,
    val splitType: SplitType = SplitType.EQUAL,
    val memberSplits: ImmutableList<MemberSplitRow> = persistentListOf(),
    val isCategorySheetOpen: Boolean = false,
    val isLoading: Boolean = false,
    val amountError: String? = null,
    val descriptionError: String? = null,
    val paidByError: String? = null,
    val splitError: String? = null,
) : UiState {
    val parsedAmount: Double get() = amount.toDoubleOrNull() ?: 0.0
    val isFormValid: Boolean
        get() = amountError == null
             && descriptionError == null
             && paidByError == null
             && splitError == null
             && amount.isNotEmpty()
             && description.isNotEmpty()
             && paidBy != null
}

sealed class AddExpenseIntent : UiIntent {
    data class LoadTripMembers(val tripId: String) : AddExpenseIntent()
    data class AmountChanged(val amount: String) : AddExpenseIntent()
    data class DescriptionChanged(val description: String) : AddExpenseIntent()
    data class CategorySelected(val category: ExpenseCategory) : AddExpenseIntent()
    data object OpenCategorySheet : AddExpenseIntent()
    data object CloseCategorySheet : AddExpenseIntent()
    data class PaidBySelected(val member: MemberUiModel) : AddExpenseIntent()
    data class SplitTypeChanged(val type: SplitType) : AddExpenseIntent()
    data class MemberIncludeToggled(val memberId: String) : AddExpenseIntent()
    data class CustomAmountChanged(val memberId: String, val amount: String) : AddExpenseIntent()
    data class PercentageChanged(val memberId: String, val percentage: Double) : AddExpenseIntent()
    data class SharesChanged(val memberId: String, val shares: Int) : AddExpenseIntent()
    data object SaveClicked : AddExpenseIntent()
    data object ClearErrors : AddExpenseIntent()
}

sealed class AddExpenseEvent : UiEvent {
    data object NavigateUp : AddExpenseEvent()
    data class ShowSnackbar(val message: String) : AddExpenseEvent()
}
