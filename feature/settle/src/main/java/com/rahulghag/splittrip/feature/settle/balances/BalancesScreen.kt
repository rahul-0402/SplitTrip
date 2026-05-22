package com.rahulghag.splittrip.feature.settle.balances

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rahulghag.splittrip.core.ui.components.avatar.AvatarSize
import com.rahulghag.splittrip.core.ui.components.avatar.MemberAvatar
import com.rahulghag.splittrip.core.ui.components.loading.SkeletonTripRow
import com.rahulghag.splittrip.core.ui.components.text.AmountText
import com.rahulghag.splittrip.core.ui.components.text.AmountType
import com.rahulghag.splittrip.core.ui.components.topbar.SplitTripTopBar
import com.rahulghag.splittrip.core.ui.extensions.CollectEvents
import com.rahulghag.splittrip.core.ui.theme.AmountTextStyle
import com.rahulghag.splittrip.core.ui.theme.Dimens
import com.rahulghag.splittrip.core.ui.theme.SplitTripTheme
import com.rahulghag.splittrip.core.ui.theme.extendedColors
import com.rahulghag.splittrip.domain.settle.model.Balance
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.abs

@Composable
fun BalancesScreen(
    onNavigateUp: () -> Unit,
    onNavigateToSettleUp: (String, String) -> Unit,
    viewModel: BalancesViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CollectEvents(viewModel.uiEvent) { event ->
        when (event) {
            BalancesEvent.NavigateUp -> onNavigateUp()
            is BalancesEvent.NavigateToSettleUp ->
                onNavigateToSettleUp(event.tripId, event.tripName)
        }
    }

    Scaffold(
        topBar = {
            SplitTripTopBar(
                title = "Balances",
                subtitle = state.tripName.ifEmpty { null },
                onNavigateUp = onNavigateUp,
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        BalancesContent(
            state = state,
            onIntent = viewModel::onIntent,
            modifier = Modifier.padding(padding),
        )
    }
}

@Composable
private fun BalancesContent(
    state: BalancesState,
    onIntent: (BalancesIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.isLoading) {
        Column(modifier = modifier.fillMaxWidth()) {
            repeat(4) { SkeletonTripRow() }
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        BalanceSummaryRow(state = state)

        Text(
            text = "PER MEMBER",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, letterSpacing = 1.5.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.padding(
                horizontal = Dimens.spaceL,
                vertical = Dimens.space2XL,
            ),
        )

        state.balances.forEachIndexed { index, balance ->
            BalanceMemberRow(balance = balance)
            if (index < state.balances.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = Dimens.spaceL),
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
            }
        }

        Spacer(Modifier.height(Dimens.space2XL))

        Button(
            onClick = { onIntent(BalancesIntent.SettleUpClicked) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.spaceL)
                .height(50.dp),
            shape = MaterialTheme.shapes.medium,
        ) {
            Icon(
                imageVector = Icons.Outlined.AccountBalance,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(Modifier.width(Dimens.spaceS))
            Text(
                text = "Settle up",
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Composable
private fun BalanceSummaryRow(state: BalancesState) {
    val extColors = MaterialTheme.extendedColors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM),
        horizontalArrangement = Arrangement.spacedBy(Dimens.spaceM),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .background(extColors.successContainer, MaterialTheme.shapes.medium)
                .padding(Dimens.spaceL),
        ) {
            Column {
                Text(
                    text = "YOU ARE OWED",
                    style = MaterialTheme.typography.labelSmall,
                    color = extColors.success,
                )
                AmountText(
                    amount = state.totalOwed,
                    type = AmountType.POSITIVE,
                    showSign = false,
                    style = AmountTextStyle.copy(fontSize = 24.sp),
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colorScheme.errorContainer, MaterialTheme.shapes.medium)
                .padding(Dimens.spaceL),
        ) {
            Column {
                Text(
                    text = "YOU OWE",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                )
                AmountText(
                    amount = state.totalOwing,
                    type = AmountType.NEGATIVE,
                    showSign = false,
                    style = AmountTextStyle.copy(fontSize = 24.sp),
                )
            }
        }
    }
}

@Composable
private fun BalanceMemberRow(balance: Balance) {
    val extColors = MaterialTheme.extendedColors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MemberAvatar(
            name = balance.memberName,
            index = balance.memberIndex,
            size = AvatarSize.MD,
        )
        Spacer(Modifier.width(Dimens.spaceM))
        Column {
            Text(
                text = balance.memberName,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = when {
                    balance.netAmount > 0 -> "owes you"
                    balance.netAmount < 0 -> "you owe"
                    else -> "settled up"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = when {
                    balance.netAmount > 0 -> extColors.success
                    balance.netAmount < 0 -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                },
            )
        }

        Spacer(Modifier.weight(1f))

        if (balance.netAmount == 0.0) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.spaceXS),
            ) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = extColors.success,
                )
                Text(
                    text = "Settled",
                    style = MaterialTheme.typography.labelLarge,
                    color = extColors.success,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        } else {
            AmountText(
                amount = abs(balance.netAmount),
                type = if (balance.netAmount > 0) AmountType.POSITIVE else AmountType.NEGATIVE,
                showSign = false,
                style = AmountTextStyle,
            )
        }
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
private fun BalancesLoadingPreview() {
    SplitTripTheme {
        BalancesContent(
            state = BalancesState(isLoading = true),
            onIntent = {},
        )
    }
}

@Preview(showBackground = true, name = "Loaded — Goa trip")
@Composable
private fun BalancesLoadedPreview() {
    SplitTripTheme {
        BalancesContent(
            state = BalancesState(
                tripId = "trip_1",
                tripName = "Goa trip 2025",
                balances = listOf(
                    Balance("m2", "Komal", 1, 520.0),
                    Balance("m3", "Arun", 2, 320.0),
                    Balance("m4", "Sara", 3, 0.0),
                ).toImmutableList(),
                totalOwed = 840.0,
                totalOwing = 0.0,
                isLoading = false,
            ),
            onIntent = {},
        )
    }
}
