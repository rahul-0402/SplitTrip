package com.rahulghag.splittrip.feature.trips.addexpense

import androidx.lifecycle.SavedStateHandle
import com.rahulghag.splittrip.core.ui.viewmodel.SplitTripViewModel
import com.rahulghag.splittrip.domain.trips.model.Member
import com.rahulghag.splittrip.domain.trips.model.MemberSplit
import com.rahulghag.splittrip.domain.trips.model.SplitType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.math.floor
import kotlin.math.roundToInt

private val FakeMembers = listOf(
    Member("m1", "Rahul", 0, "rahul@upi"),
    Member("m2", "Komal", 1, "komal@upi"),
    Member("m3", "Arun", 2, null),
    Member("m4", "Sara", 3, null),
)

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
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
        val included = state.memberSplits.filter { it.isIncluded }
        val count = included.size
        val total = state.parsedAmount

        if (count == 0) {
            updateState { copy(memberSplits = memberSplits.map { it.copy(amount = 0.0) }.toImmutableList()) }
            return
        }

        when (state.splitType) {
            SplitType.EQUAL -> {
                val base = floor(total / count * 100) / 100.0
                val remainder = ((total - base * (count - 1)) * 100).roundToInt() / 100.0
                val lastId = included.last().member.id
                updateState {
                    copy(memberSplits = memberSplits.map { row ->
                        when {
                            !row.isIncluded -> row.copy(amount = 0.0)
                            row.member.id == lastId -> row.copy(amount = remainder)
                            else -> row.copy(amount = base)
                        }
                    }.toImmutableList())
                }
            }

            SplitType.PERCENTAGE -> {
                updateState {
                    copy(memberSplits = memberSplits.map { row ->
                        row.copy(
                            amount = if (row.isIncluded)
                                (total * row.percentage / 100.0 * 100).roundToInt() / 100.0
                            else 0.0
                        )
                    }.toImmutableList())
                }
            }

            SplitType.CUSTOM -> {
                updateState {
                    copy(memberSplits = memberSplits.map { row ->
                        row.copy(amount = if (row.isIncluded) row.customAmount.toDoubleOrNull() ?: 0.0 else 0.0)
                    }.toImmutableList())
                }
            }

            SplitType.SHARES -> {
                val totalShares = included.sumOf { it.shares }.toDouble()
                if (totalShares == 0.0) return
                val amounts = mutableMapOf<String, Double>()
                var sum = 0.0
                included.dropLast(1).forEach { row ->
                    val share = floor(total * row.shares / totalShares * 100) / 100.0
                    amounts[row.member.id] = share
                    sum += share
                }
                included.lastOrNull()?.let { last ->
                    amounts[last.member.id] = ((total - sum) * 100).roundToInt() / 100.0
                }
                updateState {
                    copy(memberSplits = memberSplits.map { row ->
                        row.copy(amount = amounts[row.member.id] ?: 0.0)
                    }.toImmutableList())
                }
            }
        }
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
        val splitErr = validateSplits(state)

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

    private fun validateSplits(state: AddExpenseState): String? {
        val included = state.memberSplits.filter { it.isIncluded }
        if (included.isEmpty()) return "At least one member must be included"
        return when (state.splitType) {
            SplitType.EQUAL -> null
            SplitType.PERCENTAGE -> {
                val sum = included.sumOf { it.percentage }
                if (kotlin.math.abs(sum - 100.0) > 0.01) "Percentages must add up to 100%" else null
            }
            SplitType.CUSTOM -> {
                val sum = included.sumOf { it.customAmount.toDoubleOrNull() ?: 0.0 }
                val total = state.parsedAmount
                val label = if (total % 1.0 == 0.0) total.toInt().toString() else "%.2f".format(total)
                if (kotlin.math.abs(sum - total) > 0.01) "Amounts must add up to ₹$label" else null
            }
            SplitType.SHARES -> null
        }
    }
}
