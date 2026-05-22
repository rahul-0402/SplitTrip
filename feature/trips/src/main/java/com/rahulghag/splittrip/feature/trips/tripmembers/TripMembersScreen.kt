package com.rahulghag.splittrip.feature.trips.tripmembers

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rahulghag.splittrip.core.ui.components.avatar.AvatarSize
import com.rahulghag.splittrip.core.ui.components.avatar.MemberAvatar
import com.rahulghag.splittrip.core.ui.components.loading.SkeletonTripRow
import com.rahulghag.splittrip.core.ui.components.topbar.SplitTripTopBar
import com.rahulghag.splittrip.core.ui.extensions.CollectEvents
import com.rahulghag.splittrip.core.ui.theme.Dimens
import com.rahulghag.splittrip.core.ui.theme.JetBrainsMono
import com.rahulghag.splittrip.core.ui.theme.SplitTripTheme
import com.rahulghag.splittrip.core.ui.theme.extendedColors
import com.rahulghag.splittrip.domain.trips.model.MemberRole
import com.rahulghag.splittrip.domain.trips.model.TripMember
import kotlinx.collections.immutable.toImmutableList

@Composable
fun TripMembersScreen(
    onNavigateUp: () -> Unit,
    viewModel: TripMembersViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    CollectEvents(viewModel.uiEvent) { event ->
        when (event) {
            TripMembersEvent.NavigateUp -> onNavigateUp()

            is TripMembersEvent.CopyToClipboard -> {
                val clipboard = context.getSystemService(ClipboardManager::class.java)
                val clip = ClipData.newPlainText("Invite link", event.text)
                clipboard?.setPrimaryClip(clip)
                snackbarHostState.showSnackbar("Link copied!")
            }

            is TripMembersEvent.ShareText -> {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, event.text)
                }
                context.startActivity(Intent.createChooser(intent, "Share invite link"))
            }

            is TripMembersEvent.ShowSnackbar ->
                snackbarHostState.showSnackbar(event.message)
        }
    }

    Scaffold(
        topBar = {
            SplitTripTopBar(
                title = "Members",
                subtitle = state.tripName.ifEmpty { null },
                onNavigateUp = onNavigateUp,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        TripMembersContent(
            state = state,
            onIntent = viewModel::onIntent,
            modifier = Modifier.padding(padding),
        )
    }
}

@Composable
private fun TripMembersContent(
    state: TripMembersState,
    onIntent: (TripMembersIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.isLoading) {
        Column(modifier = modifier.fillMaxWidth()) {
            repeat(3) { SkeletonTripRow() }
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        InviteCard(state = state, onIntent = onIntent)

        // Members header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.spaceL, vertical = Dimens.space2XL),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Members",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "${state.members.size} people",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        state.members.forEachIndexed { index, member ->
            MemberListRow(member = member)
            if (index < state.members.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = Dimens.spaceL),
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
            }
        }

        Spacer(Modifier.height(80.dp))
    }
}

@Composable
private fun InviteCard(
    state: TripMembersState,
    onIntent: (TripMembersIntent) -> Unit,
) {
    val extColors = MaterialTheme.extendedColors

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.spaceL),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(modifier = Modifier.padding(Dimens.spaceL)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.spaceS),
            ) {
                Icon(
                    imageVector = Icons.Outlined.PersonAdd,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = "Invite people",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Spacer(Modifier.height(Dimens.spaceS))

            Text(
                text = "Share this link with friends to invite them to the trip",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )

            Spacer(Modifier.height(Dimens.spaceM))

            // Invite link box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.small)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                        MaterialTheme.shapes.small,
                    )
                    .padding(Dimens.spaceM),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = state.inviteLink,
                    style = MaterialTheme.typography.bodyMedium.copy(fontFamily = JetBrainsMono),
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.width(Dimens.spaceS))
                IconButton(
                    onClick = { onIntent(TripMembersIntent.CopyInviteLinkClicked) },
                    modifier = Modifier.size(32.dp),
                ) {
                    Icon(
                        imageVector = if (state.isInviteLinkCopied)
                            Icons.Outlined.CheckCircle
                        else
                            Icons.Outlined.ContentCopy,
                        contentDescription = if (state.isInviteLinkCopied) "Copied" else "Copy",
                        modifier = Modifier.size(20.dp),
                        tint = if (state.isInviteLinkCopied)
                            extColors.success
                        else
                            MaterialTheme.colorScheme.primary,
                    )
                }
            }

            Spacer(Modifier.height(Dimens.spaceM))

            OutlinedButton(
                onClick = { onIntent(TripMembersIntent.ShareInviteLinkClicked) },
                modifier = Modifier.fillMaxWidth(),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp, MaterialTheme.colorScheme.primary,
                ),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(Modifier.width(Dimens.spaceS))
                Text(
                    text = "Share invite link",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

@Composable
private fun MemberListRow(member: TripMember) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MemberAvatar(
            name = member.name,
            index = member.memberIndex,
            size = AvatarSize.MD,
        )
        Spacer(Modifier.width(Dimens.spaceM))
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.spaceS),
            ) {
                Text(
                    text = member.name,
                    style = MaterialTheme.typography.titleMedium,
                )
                if (member.role == MemberRole.ADMIN) {
                    Box(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.shapes.extraSmall,
                            )
                            .padding(horizontal = Dimens.spaceXS, vertical = 2.dp),
                    ) {
                        Text(
                            text = "Admin",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
            val upiId = member.upiId
            if (upiId != null) {
                Text(
                    text = upiId,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                Text(
                    text = "No UPI ID",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontStyle = FontStyle.Italic,
                )
            }
        }
        Text(
            text = "Joined ${formatJoinDate(member.joinedAt)}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private fun formatJoinDate(date: String): String {
    val parts = date.split("-")
    if (parts.size != 3) return date
    val months = listOf(
        "", "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
    )
    val month = months.getOrElse(parts[1].toIntOrNull() ?: 0) { parts[1] }
    return "${parts[2].toIntOrNull() ?: parts[2]} $month"
}

@Preview(showBackground = true, name = "Loading")
@Composable
private fun TripMembersLoadingPreview() {
    SplitTripTheme {
        TripMembersContent(
            state = TripMembersState(isLoading = true),
            onIntent = {},
        )
    }
}

@Preview(showBackground = true, name = "Loaded — Goa trip")
@Composable
private fun TripMembersLoadedPreview() {
    SplitTripTheme {
        TripMembersContent(
            state = TripMembersState(
                tripId = "trip_1",
                tripName = "Goa trip 2025",
                members = listOf(
                    TripMember("m1", "Rahul", 0, "rahul@upi",  MemberRole.ADMIN,  "2025-01-10"),
                    TripMember("m2", "Priya", 1, "priya@upi",  MemberRole.MEMBER, "2025-01-11"),
                    TripMember("m3", "Arun",  2, null,          MemberRole.MEMBER, "2025-01-11"),
                    TripMember("m4", "Sara",  3, null,          MemberRole.MEMBER, "2025-01-12"),
                ).toImmutableList(),
                inviteCode = "goa25xK",
                inviteLink = "splittrip.app/join/goa25xK",
                isLoading = false,
            ),
            onIntent = {},
        )
    }
}
