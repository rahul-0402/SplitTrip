package com.rahulghag.splittrip.feature.trips.addexpense

import androidx.lifecycle.SavedStateHandle
import com.rahulghag.splittrip.core.ui.viewmodel.SplitTripViewModel
import com.rahulghag.splittrip.domain.trips.model.Member
import com.rahulghag.splittrip.domain.trips.model.MemberSplit
import com.rahulghag.splittrip.domain.trips.usecase.CalculateSplitsUseCase
import com.rahulghag.splittrip.domain.trips.usecase.ValidateExpenseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import javax.inject.Inject

private val FakeMembers = listOf(
    Member("m1", "Rahul", 0, "rahul@upi"),
    Member("m2", "Komal", 1, "komal@upi"),
    Member("m3", "Arun", 2, null),
    Member("m4", "Sara", 3, null),
)

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val calculateSplits: CalculateSplitsUseCase,
    private val validateExpense: ValidateExpenseUseCase,
) : SplitTripViewModel<AddExpenseState, AddExpenseIntent, AddExpenseEvent>(
    initialState = AddExpenseState()
) {
    private val tripId: String = savedStateHandle["tripId"] ?: ""

    init {
        val initialPct = 100.0 / FakeMembers.size
        val splits = FakeMembers.map { member ->
            MemberSplit(member = member, percentage = initialPct, shares = 1)
        }.toImmutableList()
        updateState {
            copy(tripId = tripId, paidBy = FakeMembers.first(), memberSplits = splits)
        }
        recalculateSplits()
    }

    override fun onIntent(intent: AddExpenseIntent) {
        when (intent) {
            is AddExpenseIntent.LoadTripMembers -> Unit

            is AddExpenseIntent.AmountChanged -> {
                updateState { copy(amount = intent.amount, amountError = null) }
                recalculateSplits()
            }

            is AddExpenseIntent.DescriptionChanged ->
                updateState { copy(description = intent.description, descriptionError = null) }

            is AddExpenseIntent.CategorySelected ->
                updateState { copy(selectedCategory = intent.category, isCategorySheetOpen = false) }

            AddExpenseIntent.OpenCategorySheet ->
                updateState { copy(isCategorySheetOpen = true) }

            AddExpenseIntent.CloseCategorySheet ->
                updateState { copy(isCategorySheetOpen = false) }

            is AddExpenseIntent.PaidBySelected ->
                updateState { copy(paidBy = intent.member, paidByError = null) }

            is AddExpenseIntent.SplitTypeChanged -> {
                updateState { copy(splitType = intent.type) }
                recalculateSplits()
            }

            is AddExpenseIntent.MemberIncludeToggled -> {
                updateState {
                    copy(memberSplits = memberSplits.map { row ->
                        if (row.member.id == intent.memberId) row.copy(isIncluded = !row.isIncluded)
                        else row
                    }.toImmutableList())
                }
                recalculateSplits()
            }

            is AddExpenseIntent.CustomAmountChanged -> {
                updateState {
                    copy(memberSplits = memberSplits.map { row ->
                        if (row.member.id == intent.memberId) row.copy(customAmount = intent.amount)
                        else row
                    }.toImmutableList())
                }
                recalculateSplits()
            }

            is AddExpenseIntent.PercentageChanged -> {
                updateState {
                    copy(memberSplits = memberSplits.map { row ->
                        if (row.member.id == intent.memberId) row.copy(percentage = intent.percentage)
                        else row
                    }.toImmutableList())
                }
                recalculateSplits()
            }

            is AddExpenseIntent.SharesChanged -> {
                updateState {
                    copy(memberSplits = memberSplits.map { row ->
                        if (row.member.id == intent.memberId) row.copy(shares = maxOf(1, intent.shares))
                        else row
                    }.toImmutableList())
                }
                recalculateSplits()
            }

            AddExpenseIntent.SaveClicked -> save()

            AddExpenseIntent.ClearErrors ->
                updateState { copy(amountError = null, descriptionError = null, paidByError = null, splitError = null) }
        }
    }

    private fun recalculateSplits() {
        val state = currentState
        val updated = calculateSplits(state.memberSplits, state.parsedAmount, state.splitType)
        updateState { copy(memberSplits = updated.toImmutableList()) }
    }

    private fun save() {
        val state = currentState
        val amountErr = when {
            state.amount.isEmpty() -> "Amount is required"
            state.parsedAmount <= 0 -> "Amount must be greater than 0"
            else -> null
        }
        val descErr = when {
            state.description.isEmpty() -> "Description is required"
            state.description.length < 2 -> "Description is too short"
            else -> null
        }
        val paidByErr = if (state.paidBy == null) "Select who paid" else null
        val splitErr = validateExpense(state.memberSplits, state.parsedAmount, state.splitType)

        if (amountErr != null || descErr != null || paidByErr != null || splitErr != null) {
            updateState {
                copy(
                    amountError = amountErr,
                    descriptionError = descErr,
                    paidByError = paidByErr,
                    splitError = splitErr,
                )
            }
        } else {
            updateState { copy(isLoading = true) }
            launch {
                delay(800)
                sendEvent(AddExpenseEvent.NavigateUp)
            }
        }
    }

}
