package com.rahulghag.splittrip.feature.settle.settleup

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rahulghag.splittrip.core.ui.components.avatar.AvatarSize
import com.rahulghag.splittrip.core.ui.components.avatar.MemberAvatar
import com.rahulghag.splittrip.core.ui.components.button.SplitTripOutlineButton
import com.rahulghag.splittrip.core.ui.components.button.SplitTripPrimaryButton
import com.rahulghag.splittrip.core.ui.components.empty.EmptyState
import com.rahulghag.splittrip.core.ui.components.loading.SkeletonTripRow
import com.rahulghag.splittrip.core.ui.components.text.AmountText
import com.rahulghag.splittrip.core.ui.components.text.AmountType
import com.rahulghag.splittrip.core.ui.components.topbar.SplitTripTopBar
import com.rahulghag.splittrip.core.ui.extensions.CollectEvents
import com.rahulghag.splittrip.core.ui.theme.AmountTextStyle
import com.rahulghag.splittrip.core.ui.theme.Dimens
import com.rahulghag.splittrip.core.ui.theme.SplitTripTheme
import com.rahulghag.splittrip.core.ui.theme.extendedColors
import com.rahulghag.splittrip.feature.settle.model.SettlementStatus
import com.rahulghag.splittrip.feature.settle.model.SettlementUiModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
fun SettleUpScreen(
    onNavigateUp: () -> Unit,
    viewModel: SettleUpViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    CollectEvents(viewModel.uiEvent) { event ->
        when (event) {
            SettleUpEvent.NavigateUp -> onNavigateUp()

            is SettleUpEvent.LaunchUpiApp -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.upiUri))
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                } else {
                    snackbarHostState.showSnackbar("No UPI app found. Install GPay or PhonePe.")
                }
            }

            is SettleUpEvent.ShowSnackbar ->
                snackbarHostState.showSnackbar(event.message)
        }
    }

    Scaffold(
        topBar = {
            SplitTripTopBar(
                title = "Settle up",
                subtitle = state.tripName.ifEmpty { null },
                onNavigateUp = onNavigateUp,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        SettleUpContent(
            state = state,
            onIntent = viewModel::onIntent,
            modifier = Modifier.padding(padding),
        )
    }
}

@Composable
private fun SettleUpContent(
    state: SettleUpState,
    onIntent: (SettleUpIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.isLoading) {
        Column(modifier = modifier.fillMaxWidth()) {
            repeat(3) { SkeletonTripRow() }
        }
        return
    }

    val pending = state.pendingSettlements
    val confirmed = state.confirmedSettlements

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        // Info banner
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM)
                .background(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.shapes.medium)
                .padding(Dimens.spaceL),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.width(Dimens.spaceS))
            Text(
                text = "Minimum ${pending.size} transaction${if (pending.size != 1) "s" else ""} to clear all balances",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }

        if (pending.isNotEmpty()) {
            Text(
                text = "PENDING",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, letterSpacing = 1.5.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.padding(
                    horizontal = Dimens.spaceL,
                    vertical = Dimens.space2XL,
                ),
            )
            pending.forEach { settlement ->
                SettlementCard(
                    settlement = settlement,
                    onPayUpi = { onIntent(SettleUpIntent.PayViaUpiClicked(settlement)) },
                    onMarkPaid = { onIntent(SettleUpIntent.MarkAsPaidClicked(settlement.id)) },
                )
            }
        }

        if (confirmed.isNotEmpty()) {
            Text(
                text = "SETTLED",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, letterSpacing = 1.5.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.padding(
                    horizontal = Dimens.spaceL,
                    vertical = Dimens.space2XL,
                ),
            )
            confirmed.forEach { settlement ->
                ConfirmedSettlementRow(settlement = settlement)
            }
        }

        if (pending.isEmpty()) {
            EmptyState(
                icon = Icons.Outlined.CheckCircle,
                title = "All settled up!",
                subtitle = "Everyone has paid their share",
            )
        }

        Spacer(Modifier.height(Dimens.space3XL))
    }
}

@Composable
private fun SettlementCard(
    settlement: SettlementUiModel,
    onPayUpi: () -> Unit,
    onMarkPaid: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceXS),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.padding(Dimens.spaceL)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MemberAvatar(
                    name = settlement.fromMemberName,
                    index = settlement.fromMemberIndex,
                    size = AvatarSize.SM,
                )
                Spacer(Modifier.width(Dimens.spaceS))
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.width(Dimens.spaceS))
                MemberAvatar(
                    name = settlement.toMemberName,
                    index = settlement.toMemberIndex,
                    size = AvatarSize.SM,
                )
                Spacer(Modifier.weight(1f))
                AmountText(
                    amount = settlement.amount,
                    type = AmountType.NEUTRAL,
                    showSign = false,
                    style = AmountTextStyle.copy(fontSize = 20.sp),
                )
            }

            Spacer(Modifier.height(Dimens.spaceS))

            Text(
                text = "${settlement.fromMemberName} → ${settlement.toMemberName}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (settlement.toUpiId != null) {
                Text(
                    text = settlement.toUpiId,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(Modifier.height(Dimens.spaceM))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.spaceM),
            ) {
                SplitTripPrimaryButton(
                    text = if (settlement.toUpiId != null) "Pay via UPI" else "No UPI ID",
                    onClick = onPayUpi,
                    enabled = settlement.toUpiId != null,
                    modifier = Modifier.weight(1f),
                )
                SplitTripOutlineButton(
                    text = "Mark paid",
                    onClick = onMarkPaid,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun ConfirmedSettlementRow(settlement: SettlementUiModel) {
    val extColors = MaterialTheme.extendedColors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(0.6f)
            .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.spaceS),
    ) {
        MemberAvatar(
            name = settlement.fromMemberName,
            index = settlement.fromMemberIndex,
            size = AvatarSize.SM,
        )
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        MemberAvatar(
            name = settlement.toMemberName,
            index = settlement.toMemberIndex,
            size = AvatarSize.SM,
        )
        Spacer(Modifier.weight(1f))
        AmountText(
            amount = settlement.amount,
            type = AmountType.NEUTRAL,
            showSign = false,
        )
        Spacer(Modifier.width(Dimens.spaceS))
        Icon(
            imageVector = Icons.Outlined.CheckCircle,
            contentDescription = "Settled",
            modifier = Modifier.size(16.dp),
            tint = extColors.success,
        )
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
private fun SettleUpLoadingPreview() {
    SplitTripTheme {
        SettleUpContent(
            state = SettleUpState(isLoading = true),
            onIntent = {},
        )
    }
}

@Preview(showBackground = true, name = "Pending settlements")
@Composable
private fun SettleUpPendingPreview() {
    SplitTripTheme {
        SettleUpContent(
            state = SettleUpState(
                tripId = "trip_1",
                tripName = "Goa trip 2025",
                settlements = listOf(
                    SettlementUiModel(
                        id = "settle_1", tripId = "trip_1",
                        fromMemberId = "m2", fromMemberName = "Komal", fromMemberIndex = 1,
                        toMemberId = "m1", toMemberName = "Rahul", toMemberIndex = 0,
                        toUpiId = "rahul@upi", amount = 520.0, status = SettlementStatus.PENDING,
                    ),
                    SettlementUiModel(
                        id = "settle_2", tripId = "trip_1",
                        fromMemberId = "m3", fromMemberName = "Arun", fromMemberIndex = 2,
                        toMemberId = "m1", toMemberName = "Rahul", toMemberIndex = 0,
                        toUpiId = "rahul@upi", amount = 320.0, status = SettlementStatus.PENDING,
                    ),
                ).toImmutableList(),
                isLoading = false,
            ),
            onIntent = {},
        )
    }
}

@Preview(showBackground = true, name = "All settled")
@Composable
private fun SettleUpAllSettledPreview() {
    SplitTripTheme {
        SettleUpContent(
            state = SettleUpState(
                tripId = "trip_1",
                tripName = "Goa trip 2025",
                settlements = listOf(
                    SettlementUiModel(
                        id = "settle_1", tripId = "trip_1",
                        fromMemberId = "m2", fromMemberName = "Komal", fromMemberIndex = 1,
                        toMemberId = "m1", toMemberName = "Rahul", toMemberIndex = 0,
                        toUpiId = "rahul@upi", amount = 520.0, status = SettlementStatus.PENDING,
                    ),
                ).toImmutableList(),
                confirmedIds = persistentListOf("settle_1"),
                isLoading = false,
            ),
            onIntent = {},
        )
    }
}
