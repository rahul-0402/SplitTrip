package com.rahulghag.splittrip.feature.trips.expensedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.CallMade
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rahulghag.splittrip.core.ui.components.avatar.AvatarSize
import com.rahulghag.splittrip.core.ui.components.avatar.MemberAvatar
import com.rahulghag.splittrip.core.ui.components.text.AmountText
import com.rahulghag.splittrip.core.ui.components.text.AmountType
import com.rahulghag.splittrip.core.ui.components.topbar.SplitTripTopBar
import com.rahulghag.splittrip.core.ui.extensions.CollectEvents
import com.rahulghag.splittrip.core.ui.theme.AmountTextStyle
import com.rahulghag.splittrip.core.ui.theme.Dimens
import com.rahulghag.splittrip.core.ui.theme.MemberColors
import com.rahulghag.splittrip.core.ui.theme.SplitTripTheme
import com.rahulghag.splittrip.core.ui.theme.extendedColors
import com.rahulghag.splittrip.domain.trips.model.ExpenseCategory
import com.rahulghag.splittrip.domain.trips.model.ExpenseSplit
import com.rahulghag.splittrip.domain.trips.model.Expense

@Composable
fun ExpenseDetailScreen(
    onNavigateUp: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
    viewModel: ExpenseDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    CollectEvents(viewModel.uiEvent) { event ->
        when (event) {
            ExpenseDetailEvent.NavigateUp -> onNavigateUp()
            is ExpenseDetailEvent.NavigateToEditExpense -> onNavigateToEdit(event.expenseId)
            is ExpenseDetailEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
        }
    }

    Scaffold(
        topBar = {
            SplitTripTopBar(
                title = "Expense detail",
                onNavigateUp = onNavigateUp,
                actions = {
                    IconButton(onClick = { viewModel.onIntent(ExpenseDetailIntent.EditClicked) }) {
                        Icon(Icons.Outlined.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { viewModel.onIntent(ExpenseDetailIntent.DeleteClicked) }) {
                        Icon(
                            Icons.Outlined.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error,
                        )
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        ExpenseDetailContent(
            state = state,
            onIntent = viewModel::onIntent,
            modifier = Modifier.padding(padding),
        )
    }

    val expense = state.expense
    if (state.showDeleteConfirmDialog && expense != null) {
        AlertDialog(
            onDismissRequest = { viewModel.onIntent(ExpenseDetailIntent.DismissDeleteDialog) },
            title = { Text("Delete expense?") },
            text = {
                Text(
                    text = "This will remove \"${expense.title}\" and recalculate all balances.",
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.onIntent(ExpenseDetailIntent.ConfirmDelete) },
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.onIntent(ExpenseDetailIntent.DismissDeleteDialog) },
                ) {
                    Text("Cancel")
                }
            },
        )
    }
}

@Composable
private fun ExpenseDetailContent(
    state: ExpenseDetailState,
    onIntent: (ExpenseDetailIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    val expense = state.expense ?: return

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        ExpenseHeader(expense = expense)
        SplitBreakdownSection(expense = expense)
        ExpenseSummaryCard(expense = expense)
        Spacer(Modifier.height(Dimens.space3XL))
    }
}

@Composable
private fun ExpenseHeader(expense: Expense) {
    val extColors = MaterialTheme.extendedColors
    val categoryBg = when (expense.category) {
        ExpenseCategory.FOOD -> extColors.memberContainerColors[2]
        ExpenseCategory.STAY -> MaterialTheme.colorScheme.primaryContainer
        ExpenseCategory.TRAVEL -> extColors.warningContainer
        ExpenseCategory.FUN -> extColors.memberContainerColors[0]
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.spaceL),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.spaceL),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(color = categoryBg, shape = MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = expense.category.emoji, fontSize = 28.sp)
            }

            Spacer(Modifier.height(Dimens.spaceM))

            Text(
                text = expense.title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(Dimens.spaceXS))
            Text(
                text = formatDate(expense.date),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(Modifier.height(Dimens.spaceM))

            AmountText(
                amount = expense.amount,
                type = AmountType.NEUTRAL,
                showSign = false,
                style = AmountTextStyle.copy(fontSize = 32.sp),
            )

            Spacer(Modifier.height(Dimens.spaceS))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                MemberAvatar(
                    name = expense.paidByName,
                    index = expense.paidByIndex,
                    size = AvatarSize.SM,
                )
                Spacer(Modifier.width(Dimens.spaceS))
                Text(
                    text = "paid by ${expense.paidByName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(Modifier.height(Dimens.spaceS))

            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.spaceS),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TagChip(
                    text = expense.splitType.lowercase().replaceFirstChar { it.uppercase() },
                )
                TagChip(
                    text = "${expense.category.emoji} ${expense.category.name.lowercase().replaceFirstChar { it.uppercase() }}",
                )
            }
        }
    }
}

@Composable
private fun SplitBreakdownSection(expense: Expense) {
    if (expense.splits.isEmpty()) return

    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(Modifier.height(Dimens.spaceS))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.spaceL),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Split breakdown",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "${expense.splits.size} people",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Spacer(Modifier.height(Dimens.spaceS))
        expense.splits.forEach { split ->
            SplitBreakdownRow(split = split, totalAmount = expense.amount)
        }
    }
}

@Composable
private fun SplitBreakdownRow(
    split: ExpenseSplit,
    totalAmount: Double,
) {
    val fraction = if (totalAmount > 0) (split.amount / totalAmount).toFloat().coerceIn(0f, 1f) else 0f

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceS),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.spaceM),
    ) {
        MemberAvatar(
            name = split.memberName,
            index = split.memberIndex,
            size = AvatarSize.SM,
        )
        Text(
            text = split.memberName,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.width(60.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        SplitBar(
            fraction = fraction,
            memberIndex = split.memberIndex,
            modifier = Modifier.weight(1f),
        )
        Column(horizontalAlignment = Alignment.End) {
            AmountText(
                amount = split.amount,
                type = AmountType.NEUTRAL,
                showSign = false,
                style = AmountTextStyle.copy(fontSize = 14.sp),
            )
            Text(
                text = "${split.percentage.toInt()}%",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun SplitBar(
    fraction: Float,
    memberIndex: Int,
    modifier: Modifier = Modifier,
) {
    val color = MemberColors[memberIndex % MemberColors.size]
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(MaterialTheme.shapes.extraSmall)
            .background(MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction)
                .fillMaxHeight()
                .background(color),
        )
    }
}

@Composable
private fun ExpenseSummaryCard(expense: Expense) {
    val extColors = MaterialTheme.extendedColors
    val isPayer = expense.paidByName == "Rahul"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (isPayer) extColors.successContainer
                             else MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.spaceL),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                if (isPayer) {
                    Text(
                        text = "YOU PAID · GET BACK",
                        style = MaterialTheme.typography.labelSmall,
                        color = extColors.success,
                    )
                    AmountText(
                        amount = expense.amount - expense.yourShare,
                        type = AmountType.POSITIVE,
                        showSign = false,
                        style = AmountTextStyle.copy(fontSize = 24.sp),
                    )
                    Text(
                        text = "from ${expense.splits.size - 1} others",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    Text(
                        text = "YOUR SHARE",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    AmountText(
                        amount = expense.yourShare,
                        type = AmountType.NEGATIVE,
                        showSign = false,
                        style = AmountTextStyle.copy(fontSize = 24.sp),
                    )
                    Text(
                        text = "owed to ${expense.paidByName}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Icon(
                imageVector = if (isPayer) Icons.Outlined.AccountBalanceWallet
                              else Icons.Outlined.CallMade,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = if (isPayer) extColors.success
                       else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun TagChip(text: String) {
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.extraSmall,
            )
            .padding(horizontal = Dimens.spaceS, vertical = 2.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private fun formatDate(date: String): String {
    val parts = date.split("-")
    if (parts.size != 3) return date
    val month = when (parts[1]) {
        "01" -> "Jan"; "02" -> "Feb"; "03" -> "Mar"; "04" -> "Apr"
        "05" -> "May"; "06" -> "Jun"; "07" -> "Jul"; "08" -> "Aug"
        "09" -> "Sep"; "10" -> "Oct"; "11" -> "Nov"; "12" -> "Dec"
        else -> parts[1]
    }
    return "${parts[2].toInt()} $month ${parts[0]}"
}

@Preview(showBackground = true, name = "Loading")
@Composable
private fun LoadingPreview() {
    SplitTripTheme {
        ExpenseDetailContent(
            state = ExpenseDetailState(isLoading = true),
            onIntent = {},
        )
    }
}

@Preview(showBackground = true, name = "Rahul paid — gets back")
@Composable
private fun RahulPaidPreview() {
    SplitTripTheme {
        ExpenseDetailContent(
            state = ExpenseDetailState(
                isLoading = false,
                expense = Expense(
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
                    splits = listOf(
                        ExpenseSplit("m1", "Rahul", 0, 700.0, 25.0),
                        ExpenseSplit("m2", "Komal", 1, 700.0, 25.0),
                        ExpenseSplit("m3", "Arun",  2, 700.0, 25.0),
                        ExpenseSplit("m4", "Sara",  3, 700.0, 25.0),
                    ),
                ),
            ),
            onIntent = {},
        )
    }
}

@Preview(showBackground = true, name = "Komal paid — you owe")
@Composable
private fun KomalPaidPreview() {
    SplitTripTheme {
        ExpenseDetailContent(
            state = ExpenseDetailState(
                isLoading = false,
                expense = Expense(
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
                    splits = listOf(
                        ExpenseSplit("m1", "Rahul", 0, 600.0, 37.5),
                        ExpenseSplit("m2", "Komal", 1, 400.0, 25.0),
                        ExpenseSplit("m3", "Arun",  2, 400.0, 25.0),
                        ExpenseSplit("m4", "Sara",  3, 200.0, 12.5),
                    ),
                ),
            ),
            onIntent = {},
        )
    }
}
