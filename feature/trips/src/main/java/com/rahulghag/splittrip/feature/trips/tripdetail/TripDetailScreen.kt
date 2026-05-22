package com.rahulghag.splittrip.feature.trips.tripdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rahulghag.splittrip.core.ui.components.avatar.AvatarSize
import com.rahulghag.splittrip.core.ui.components.avatar.AvatarStack
import com.rahulghag.splittrip.core.ui.components.avatar.MemberAvatar
import com.rahulghag.splittrip.core.ui.components.empty.EmptyState
import com.rahulghag.splittrip.core.ui.components.loading.SkeletonExpenseRow
import com.rahulghag.splittrip.core.ui.components.text.AmountText
import com.rahulghag.splittrip.core.ui.components.text.AmountType
import com.rahulghag.splittrip.core.ui.components.topbar.SplitTripTopBar
import com.rahulghag.splittrip.core.ui.extensions.CollectEvents
import com.rahulghag.splittrip.core.ui.theme.AmountTextStyle
import com.rahulghag.splittrip.core.ui.theme.Dimens
import com.rahulghag.splittrip.core.ui.theme.SplitTripTheme
import com.rahulghag.splittrip.core.ui.theme.extendedColors
import com.rahulghag.splittrip.domain.trips.model.ExpenseCategory
import com.rahulghag.splittrip.domain.trips.model.Expense
import com.rahulghag.splittrip.domain.trips.model.TripStatus
import com.rahulghag.splittrip.domain.trips.model.Trip
import kotlinx.collections.immutable.toImmutableList

@Composable
fun TripDetailScreen(
    onNavigateUp: () -> Unit,
    onNavigateToExpenseDetail: (String) -> Unit,
    onNavigateToAddExpense: (String) -> Unit,
    onNavigateToBalances: (String, String) -> Unit,
    onNavigateToTripMembers: (String, String) -> Unit,
    viewModel: TripDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CollectEvents(viewModel.uiEvent) { event ->
        when (event) {
            is TripDetailEvent.NavigateToExpenseDetail ->
                onNavigateToExpenseDetail(event.expenseId)
            is TripDetailEvent.NavigateToAddExpense ->
                onNavigateToAddExpense(event.tripId)
            is TripDetailEvent.NavigateToBalances ->
                onNavigateToBalances(event.tripId, event.tripName)
            is TripDetailEvent.NavigateToTripMembers ->
                onNavigateToTripMembers(event.tripId, event.tripName)
            TripDetailEvent.NavigateUp ->
                onNavigateUp()
        }
    }

    Scaffold(
        topBar = {
            SplitTripTopBar(
                title = state.trip?.name ?: "Trip detail",
                subtitle = state.trip?.let {
                    "${it.memberCount} members · ${it.expenseCount} expenses"
                },
                onNavigateUp = onNavigateUp,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onIntent(TripDetailIntent.AddExpenseClicked) },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
                modifier = Modifier.size(56.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Add expense",
                    tint = Color.White,
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        TripDetailContent(
            state = state,
            onIntent = viewModel::onIntent,
            modifier = Modifier.padding(padding),
        )
    }
}

@Composable
private fun TripDetailContent(
    state: TripDetailState,
    onIntent: (TripDetailIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        if (state.isLoading) {
            items(5) { SkeletonExpenseRow() }
        } else {
            item {
                TripHeroCard(state = state, onIntent = onIntent)
            }
            item {
                OutlinedButton(
                    onClick = { onIntent(TripDetailIntent.BalancesClicked) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccountBalance,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(Modifier.width(Dimens.spaceS))
                    Text(
                        text = "View balances & settle up",
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
            item {
                CategoryFilterRow(state = state, onIntent = onIntent)
            }
            if (state.displayExpenses.isEmpty()) {
                item {
                    EmptyState(
                        icon = Icons.Outlined.ReceiptLong,
                        title = "No expenses yet",
                        subtitle = "Add the first expense for this trip",
                        actionLabel = "Add expense",
                        onAction = { onIntent(TripDetailIntent.AddExpenseClicked) },
                    )
                }
            } else {
                items(state.displayExpenses, key = { it.id }) { expense ->
                    ExpenseListItem(
                        expense = expense,
                        onClick = { onIntent(TripDetailIntent.ExpenseClicked(expense.id)) },
                    )
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
private fun TripHeroCard(state: TripDetailState, onIntent: (TripDetailIntent) -> Unit) {
    val extColors = MaterialTheme.extendedColors
    val trip = state.trip
    val balance = trip?.yourBalance ?: 0.0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.spaceL),
            verticalAlignment = Alignment.Top,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "TOTAL SPENT",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                AmountText(
                    amount = trip?.totalAmount ?: 0.0,
                    type = AmountType.NEUTRAL,
                    showSign = false,
                    style = AmountTextStyle.copy(fontSize = 32.sp),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimens.spaceM),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AvatarStack(
                        names = trip?.members?.map { it.name } ?: emptyList(),
                        size = AvatarSize.SM,
                        maxVisible = 4,
                    )
                    Spacer(Modifier.width(Dimens.spaceS))
                    Text(
                        text = "${trip?.members?.size ?: 0} members",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.weight(1f))
                    TextButton(
                        onClick = { onIntent(TripDetailIntent.ManageMembersClicked) },
                    ) {
                        Text(
                            text = "Manage →",
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
            }
            Spacer(Modifier.width(Dimens.spaceM))
            Box(
                modifier = Modifier
                    .background(
                        color = when {
                            balance > 0 -> extColors.successContainer
                            balance < 0 -> MaterialTheme.colorScheme.errorContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = MaterialTheme.shapes.small,
                    )
                    .padding(horizontal = Dimens.spaceM, vertical = Dimens.spaceS),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = when {
                            balance > 0 -> "YOU GET BACK"
                            balance < 0 -> "YOU OWE"
                            else -> "SETTLED"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = when {
                            balance > 0 -> extColors.success
                            balance < 0 -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        },
                    )
                    if (balance != 0.0) {
                        AmountText(amount = balance)
                    } else {
                        Text(
                            text = "All clear ✓",
                            style = MaterialTheme.typography.bodyMedium,
                            color = extColors.success,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryFilterRow(
    state: TripDetailState,
    onIntent: (TripDetailIntent) -> Unit,
) {
    val categoriesWithExpenses = remember(state.expenses) {
        state.expenses.map { it.category }.distinct()
    }

    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceS),
        horizontalArrangement = Arrangement.spacedBy(Dimens.spaceS),
    ) {
        FilterChip(
            selected = state.selectedCategory == null,
            onClick = { onIntent(TripDetailIntent.CategoryFilterSelected(null)) },
            label = { Text("All") },
            colors = FilterChipDefaults.filterChipColors(
                containerColor = MaterialTheme.colorScheme.surface,
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = Color.White,
            ),
        )
        categoriesWithExpenses.forEach { category ->
            FilterChip(
                selected = state.selectedCategory == category,
                onClick = { onIntent(TripDetailIntent.CategoryFilterSelected(category)) },
                label = {
                    Text("${category.emoji} ${category.name.lowercase().replaceFirstChar { it.uppercase() }}")
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = Color.White,
                ),
            )
        }
    }
}

@Composable
private fun ExpenseListItem(
    expense: Expense,
    onClick: () -> Unit,
) {
    val extColors = MaterialTheme.extendedColors
    val categoryBg = when (expense.category) {
        ExpenseCategory.FOOD -> extColors.memberContainerColors[2]
        ExpenseCategory.STAY -> MaterialTheme.colorScheme.primaryContainer
        ExpenseCategory.TRAVEL -> extColors.warningContainer
        ExpenseCategory.FUN -> extColors.memberContainerColors[0]
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    Column {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = Dimens.spaceL, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.spaceM),
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = categoryBg,
                        shape = MaterialTheme.shapes.small,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = expense.category.emoji,
                    fontSize = 20.sp,
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = expense.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.spaceXS),
                ) {
                    MemberAvatar(
                        name = expense.paidByName,
                        index = expense.paidByIndex,
                        size = AvatarSize.XS,
                    )
                    Text(
                        text = "${expense.paidByName} · ${expense.splitType.lowercase().replaceFirstChar { it.uppercase() }}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                AmountText(
                    amount = expense.amount,
                    type = AmountType.NEUTRAL,
                    showSign = false,
                )
                val shareText = if (expense.yourShare % 1.0 == 0.0) {
                    expense.yourShare.toInt().toString()
                } else {
                    String.format("%.2f", expense.yourShare)
                }
                Text(
                    text = "your ₹$shareText",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = Dimens.spaceL),
        color = MaterialTheme.colorScheme.outlineVariant,
    )
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
private fun TripDetailLoadingPreview() {
    SplitTripTheme {
        TripDetailContent(
            state = TripDetailState(isLoading = true),
            onIntent = {},
        )
    }
}

@Preview(showBackground = true, name = "Loaded — Goa trip")
@Composable
private fun TripDetailLoadedPreview() {
    SplitTripTheme {
        TripDetailContent(
            state = TripDetailState(
                trip = Trip(
                    id = "trip_1",
                    name = "Goa trip 2025",
                    icon = "✈️",
                    memberCount = 4,
                    expenseCount = 6,
                    totalAmount = 18420.0,
                    yourBalance = 840.0,
                    status = TripStatus.ACTIVE,
                ),
                expenses = listOf(
                    Expense(
                        id = "exp_1",
                        tripId = "trip_1",
                        title = "Dinner at Britto's",
                        category = ExpenseCategory.FOOD,
                        amount = 2800.0,
                        paidByName = "Rahul",
                        paidByIndex = 0,
                        splitType = "EQUAL",
                        yourShare = 700.0,
                        date = "2025-01-14",
                        memberCount = 4,
                    ),
                    Expense(
                        id = "exp_2",
                        tripId = "trip_1",
                        title = "Hotel – night 2",
                        category = ExpenseCategory.STAY,
                        amount = 4200.0,
                        paidByName = "Komal",
                        paidByIndex = 1,
                        splitType = "EQUAL",
                        yourShare = 1050.0,
                        date = "2025-01-15",
                        memberCount = 4,
                    ),
                    Expense(
                        id = "exp_3",
                        tripId = "trip_1",
                        title = "Scooter rental",
                        category = ExpenseCategory.TRAVEL,
                        amount = 1600.0,
                        paidByName = "Arun",
                        paidByIndex = 2,
                        splitType = "CUSTOM",
                        yourShare = 600.0,
                        date = "2025-01-15",
                        memberCount = 4,
                    ),
                    Expense(
                        id = "exp_4",
                        tripId = "trip_1",
                        title = "Jet ski – Calangute",
                        category = ExpenseCategory.FUN,
                        amount = 3600.0,
                        paidByName = "Sara",
                        paidByIndex = 3,
                        splitType = "EQUAL",
                        yourShare = 900.0,
                        date = "2025-01-16",
                        memberCount = 4,
                    ),
                ).toImmutableList(),
                isLoading = false,
            ),
            onIntent = {},
        )
    }
}
