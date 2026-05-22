package com.rahulghag.splittrip.feature.trips.addexpense

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Notes
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rahulghag.splittrip.core.ui.components.avatar.AvatarSize
import com.rahulghag.splittrip.core.ui.components.avatar.MemberAvatar
import com.rahulghag.splittrip.core.ui.components.input.SplitTripTextField
import com.rahulghag.splittrip.core.ui.components.text.AmountText
import com.rahulghag.splittrip.core.ui.components.text.AmountType
import com.rahulghag.splittrip.core.ui.components.topbar.SplitTripTopBar
import com.rahulghag.splittrip.core.ui.extensions.CollectEvents
import com.rahulghag.splittrip.core.ui.theme.Dimens
import com.rahulghag.splittrip.core.ui.theme.SplitTripTheme
import com.rahulghag.splittrip.core.ui.theme.extendedColors
import com.rahulghag.splittrip.domain.trips.model.Member
import com.rahulghag.splittrip.domain.trips.model.MemberSplit
import com.rahulghag.splittrip.domain.trips.model.SplitType
import com.rahulghag.splittrip.domain.trips.model.ExpenseCategory
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    onNavigateUp: () -> Unit,
    viewModel: AddExpenseViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CollectEvents(viewModel.uiEvent) { event ->
        when (event) {
            AddExpenseEvent.NavigateUp -> onNavigateUp()
            is AddExpenseEvent.ShowSnackbar -> Unit
        }
    }

    Scaffold(
        topBar = {
            SplitTripTopBar(
                title = "Add expense",
                onNavigateUp = onNavigateUp,
                actions = {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(20.dp)
                                .padding(end = Dimens.spaceXS),
                            strokeWidth = 2.dp,
                        )
                    } else {
                        TextButton(
                            onClick = { viewModel.onIntent(AddExpenseIntent.SaveClicked) },
                        ) {
                            Text(
                                text = "Save",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.labelLarge,
                            )
                        }
                    }
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        AddExpenseContent(
            state = state,
            onIntent = viewModel::onIntent,
            modifier = Modifier.padding(padding),
        )
    }

    if (state.isCategorySheetOpen) {
        CategoryBottomSheet(
            selectedCategory = state.selectedCategory,
            onCategorySelected = { viewModel.onIntent(AddExpenseIntent.CategorySelected(it)) },
            onDismiss = { viewModel.onIntent(AddExpenseIntent.CloseCategorySheet) },
        )
    }
}

@Composable
private fun AddExpenseContent(
    state: AddExpenseState,
    onIntent: (AddExpenseIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding(),
    ) {
        AmountInputSection(state = state, onIntent = onIntent)
        DetailsCard(state = state, onIntent = onIntent)
        SplitSection(state = state, onIntent = onIntent)
        Spacer(Modifier.height(Dimens.space3XL))
    }
}

@Composable
private fun AmountInputSection(
    state: AddExpenseState,
    onIntent: (AddExpenseIntent) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Dimens.space3XL),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "₹",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = if (state.amount.isEmpty()) "0" else state.amount,
                    style = MaterialTheme.typography.displayLarge,
                    color = if (state.amount.isEmpty())
                        MaterialTheme.colorScheme.onSurfaceVariant
                    else
                        MaterialTheme.colorScheme.onSurface,
                )
            }
            if (state.amountError != null) {
                Text(
                    text = state.amountError,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                )
            } else {
                Text(
                    text = "tap to edit",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            BasicTextField(
                value = state.amount,
                onValueChange = { raw ->
                    val dotIdx = raw.indexOf('.')
                    val filtered = if (dotIdx == -1) {
                        raw.filter { it.isDigit() }
                    } else {
                        raw.substring(0, dotIdx + 1).filter { it.isDigit() || it == '.' } +
                            raw.substring(dotIdx + 1).filter { it.isDigit() }
                    }
                    onIntent(AddExpenseIntent.AmountChanged(filtered))
                },
                modifier = Modifier
                    .size(1.dp)
                    .alpha(0f)
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done,
                ),
            )
        }
    }
}

@Composable
private fun DetailsCard(
    state: AddExpenseState,
    onIntent: (AddExpenseIntent) -> Unit,
) {
    val extColors = MaterialTheme.extendedColors

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(modifier = Modifier.padding(Dimens.spaceL)) {
            SplitTripTextField(
                value = state.description,
                onValueChange = { onIntent(AddExpenseIntent.DescriptionChanged(it)) },
                label = "What was this for?",
                placeholder = "Dinner at Britto's",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Notes,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                errorText = state.descriptionError,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = Dimens.spaceM),
                color = MaterialTheme.colorScheme.outlineVariant,
            )

            // Category row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onIntent(AddExpenseIntent.OpenCategorySheet) }
                    .padding(vertical = Dimens.spaceS),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.spaceM),
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = categoryContainerColor(state.selectedCategory, extColors),
                            shape = CircleShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = state.selectedCategory.emoji, fontSize = 18.sp)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "CATEGORY",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = state.selectedCategory.name.lowercase().replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = Dimens.spaceM),
                color = MaterialTheme.colorScheme.outlineVariant,
            )

            // Paid by
            Text(
                text = "PAID BY",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(Dimens.spaceS))
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(Dimens.spaceS),
            ) {
                state.memberSplits.forEach { splitRow ->
                    val member = splitRow.member
                    val isSelected = state.paidBy?.id == member.id
                    Surface(
                        onClick = { onIntent(AddExpenseIntent.PaidBySelected(member)) },
                        shape = MaterialTheme.shapes.extraLarge,
                        color = if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surface,
                        border = if (!isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                                 else null,
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                horizontal = Dimens.spaceM,
                                vertical = Dimens.spaceXS,
                            ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Dimens.spaceXS),
                        ) {
                            MemberAvatar(
                                name = member.name,
                                index = member.index,
                                size = AvatarSize.XS,
                            )
                            Text(
                                text = member.name,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isSelected) Color.White
                                        else MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }
            }
            if (state.paidByError != null) {
                Spacer(Modifier.height(Dimens.spaceXS))
                Text(
                    text = state.paidByError,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@Composable
private fun SplitSection(
    state: AddExpenseState,
    onIntent: (AddExpenseIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Dimens.space2XL),
    ) {
        Text(
            text = "SPLIT",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = Dimens.spaceL),
        )
        Spacer(Modifier.height(Dimens.spaceS))

        // Split type tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.spaceL),
            horizontalArrangement = Arrangement.spacedBy(Dimens.spaceXS),
        ) {
            SplitType.entries.forEach { type ->
                val isSelected = state.splitType == type
                Surface(
                    onClick = { onIntent(AddExpenseIntent.SplitTypeChanged(type)) },
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.small,
                    color = if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surface,
                    border = if (!isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                             else null,
                ) {
                    Text(
                        text = when (type) {
                            SplitType.EQUAL -> "Equal"
                            SplitType.PERCENTAGE -> "%"
                            SplitType.CUSTOM -> "Custom"
                            SplitType.SHARES -> "Shares"
                        },
                        modifier = Modifier
                            .padding(vertical = Dimens.spaceS)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isSelected) Color.White
                                else MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }

        Spacer(Modifier.height(Dimens.spaceS))

        // Split info text
        val includedCount = state.memberSplits.count { it.isIncluded }
        val totalLabel = formatShareText(state.parsedAmount)
        Text(
            text = when (state.splitType) {
                SplitType.EQUAL -> {
                    val per = if (includedCount > 0) state.parsedAmount / includedCount else 0.0
                    "Each person pays ₹${formatShareText(per)}"
                }
                SplitType.PERCENTAGE -> "Percentages must add up to 100%"
                SplitType.CUSTOM -> "Amounts must add up to ₹$totalLabel"
                SplitType.SHARES -> "Split by number of shares"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = Dimens.spaceL),
        )

        Spacer(Modifier.height(Dimens.spaceM))

        state.memberSplits.forEach { splitRow ->
            MemberSplitItem(
                splitRow = splitRow,
                splitType = state.splitType,
                onIncludeToggled = { onIntent(AddExpenseIntent.MemberIncludeToggled(splitRow.member.id)) },
                onCustomAmountChanged = { onIntent(AddExpenseIntent.CustomAmountChanged(splitRow.member.id, it)) },
                onPercentageChanged = { onIntent(AddExpenseIntent.PercentageChanged(splitRow.member.id, it)) },
                onSharesChanged = { onIntent(AddExpenseIntent.SharesChanged(splitRow.member.id, it)) },
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceS),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceXS),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Total",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            AmountText(
                amount = state.parsedAmount,
                type = AmountType.NEUTRAL,
                showSign = false,
            )
        }
        if (state.splitError != null) {
            Text(
                text = state.splitError,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = Dimens.spaceL),
            )
        }
    }
}

@Composable
private fun MemberSplitItem(
    splitRow: MemberSplit,
    splitType: SplitType,
    onIncludeToggled: () -> Unit,
    onCustomAmountChanged: (String) -> Unit,
    onPercentageChanged: (Double) -> Unit,
    onSharesChanged: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceXS),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.spaceS),
    ) {
        Checkbox(
            checked = splitRow.isIncluded,
            onCheckedChange = { onIncludeToggled() },
        )
        MemberAvatar(
            name = splitRow.member.name,
            index = splitRow.member.index,
            size = AvatarSize.SM,
        )
        Text(
            text = splitRow.member.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface.copy(
                alpha = if (splitRow.isIncluded) 1f else 0.4f,
            ),
        )

        when (splitType) {
            SplitType.EQUAL -> {
                Box(modifier = Modifier.alpha(if (splitRow.isIncluded) 1f else 0.4f)) {
                    AmountText(
                        amount = splitRow.amount,
                        type = AmountType.NEUTRAL,
                        showSign = false,
                    )
                }
            }

            SplitType.PERCENTAGE -> {
                val pctText = if (splitRow.percentage == 0.0) ""
                              else if (splitRow.percentage % 1.0 == 0.0) splitRow.percentage.toInt().toString()
                              else "%.1f".format(splitRow.percentage)
                OutlinedTextField(
                    value = pctText,
                    onValueChange = { onPercentageChanged(it.toDoubleOrNull() ?: 0.0) },
                    enabled = splitRow.isIncluded,
                    suffix = { Text("%", style = MaterialTheme.typography.bodySmall) },
                    modifier = Modifier.width(80.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    ),
                )
            }

            SplitType.CUSTOM -> {
                OutlinedTextField(
                    value = splitRow.customAmount,
                    onValueChange = { raw ->
                        val filtered = raw.filter { it.isDigit() || it == '.' }
                        onCustomAmountChanged(filtered)
                    },
                    enabled = splitRow.isIncluded,
                    prefix = { Text("₹", style = MaterialTheme.typography.bodySmall) },
                    modifier = Modifier.width(100.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    ),
                )
            }

            SplitType.SHARES -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.spaceXS),
                ) {
                    IconButton(
                        onClick = { onSharesChanged(splitRow.shares - 1) },
                        enabled = splitRow.isIncluded && splitRow.shares > 1,
                        modifier = Modifier.size(32.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Remove,
                            contentDescription = "Decrease",
                            modifier = Modifier.size(16.dp),
                        )
                    }
                    Text(
                        text = splitRow.shares.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.widthIn(min = 24.dp),
                        textAlign = TextAlign.Center,
                    )
                    IconButton(
                        onClick = { onSharesChanged(splitRow.shares + 1) },
                        enabled = splitRow.isIncluded,
                        modifier = Modifier.size(32.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = "Increase",
                            modifier = Modifier.size(16.dp),
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryBottomSheet(
    selectedCategory: ExpenseCategory,
    onCategorySelected: (ExpenseCategory) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Text(
            text = "Select category",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(Dimens.spaceL),
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .padding(horizontal = Dimens.spaceL),
            contentPadding = PaddingValues(bottom = Dimens.space3XL),
        ) {
            items(ExpenseCategory.entries.toList()) { category ->
                val isSelected = category == selectedCategory
                Column(
                    modifier = Modifier
                        .clickable { onCategorySelected(category) }
                        .padding(Dimens.spaceXS)
                        .background(
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                    else Color.Transparent,
                            shape = MaterialTheme.shapes.medium,
                        )
                        .padding(Dimens.spaceM),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(text = category.emoji, fontSize = 32.sp)
                    Spacer(Modifier.height(Dimens.spaceXS))
                    Text(
                        text = category.name.lowercase().replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
private fun categoryContainerColor(
    category: ExpenseCategory,
    extColors: com.rahulghag.splittrip.core.ui.theme.SplitTripExtendedColors,
): Color = when (category) {
    ExpenseCategory.FOOD -> extColors.memberContainerColors[2]
    ExpenseCategory.STAY -> extColors.memberContainerColors[4]
    ExpenseCategory.TRAVEL -> extColors.warningContainer
    ExpenseCategory.FUN -> extColors.memberContainerColors[0]
    else -> extColors.infoContainer
}

private fun formatShareText(amount: Double): String =
    if (amount % 1.0 == 0.0) amount.toInt().toString()
    else "%.2f".format(amount)

private val previewMembers = listOf(
    Member("m1", "Rahul", 0, "rahul@upi"),
    Member("m2", "Komal", 1, "komal@upi"),
    Member("m3", "Arun", 2, null),
    Member("m4", "Sara", 3, null),
)

@Preview(showBackground = true, name = "Default (empty form)")
@Composable
private fun AddExpenseDefaultPreview() {
    SplitTripTheme {
        AddExpenseContent(
            state = AddExpenseState(
                memberSplits = previewMembers.map { member ->
                    MemberSplit(member = member, percentage = 25.0, shares = 1)
                }.toImmutableList(),
                paidBy = previewMembers.first(),
            ),
            onIntent = {},
        )
    }
}

@Preview(showBackground = true, name = "Filled — equal split")
@Composable
private fun AddExpenseFilledPreview() {
    SplitTripTheme {
        AddExpenseContent(
            state = AddExpenseState(
                amount = "2800",
                description = "Dinner at Britto's",
                selectedCategory = ExpenseCategory.FOOD,
                paidBy = previewMembers.first(),
                splitType = SplitType.EQUAL,
                memberSplits = previewMembers.map { member ->
                    MemberSplit(
                        member = member,
                        amount = 700.0,
                        percentage = 25.0,
                        shares = 1,
                        isIncluded = true,
                    )
                }.toImmutableList(),
                isLoading = false,
            ),
            onIntent = {},
        )
    }
}
