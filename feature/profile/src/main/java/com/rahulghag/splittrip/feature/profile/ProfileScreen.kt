package com.rahulghag.splittrip.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.CurrencyRupee
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rahulghag.splittrip.core.ui.components.avatar.AvatarSize
import com.rahulghag.splittrip.core.ui.components.avatar.MemberAvatar
import com.rahulghag.splittrip.core.ui.components.loading.SkeletonBox
import com.rahulghag.splittrip.core.ui.components.loading.shimmerBrush
import com.rahulghag.splittrip.core.ui.components.text.AmountText
import com.rahulghag.splittrip.core.ui.components.text.AmountType
import com.rahulghag.splittrip.core.ui.components.topbar.SplitTripTopBar
import com.rahulghag.splittrip.core.ui.extensions.CollectEvents
import com.rahulghag.splittrip.core.ui.theme.AmountTextStyle
import com.rahulghag.splittrip.core.ui.theme.Dimens
import com.rahulghag.splittrip.core.ui.theme.SplitTripTheme
import com.rahulghag.splittrip.domain.profile.model.Profile

@Composable
fun ProfileScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToDesignSystem: () -> Unit,
    onNavigateToCounter: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    CollectEvents(viewModel.uiEvent) { event ->
        when (event) {
            ProfileEvent.NavigateToLogin -> onNavigateToLogin()
            ProfileEvent.NavigateToDesignSystem -> onNavigateToDesignSystem()
            ProfileEvent.NavigateToCounter -> onNavigateToCounter()
            is ProfileEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
        }
    }

    Scaffold(
        topBar = {
            SplitTripTopBar(title = "Profile")
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        ProfileContent(
            state = state,
            onIntent = viewModel::onIntent,
            modifier = Modifier.padding(padding),
        )
    }

    if (state.showSignOutDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onIntent(ProfileIntent.DismissSignOutDialog) },
            title = { Text("Sign out?") },
            text = { Text("You'll need to sign in again to access your trips.") },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.onIntent(ProfileIntent.ConfirmSignOut) },
                ) {
                    Text("Sign out", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.onIntent(ProfileIntent.DismissSignOutDialog) },
                ) {
                    Text("Cancel")
                }
            },
        )
    }
}

@Composable
private fun ProfileContent(
    state: ProfileState,
    onIntent: (ProfileIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.isLoading) {
        ProfileLoadingSkeleton(modifier = modifier)
        return
    }

    val profile = state.profile ?: return

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        ProfileHeader(profile = profile)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM),
            horizontalArrangement = Arrangement.spacedBy(Dimens.spaceM),
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                value = {
                    Text(
                        text = profile.totalTrips.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                    )
                },
                label = "Total trips",
            )
            StatCard(
                modifier = Modifier.weight(1f),
                value = {
                    AmountText(
                        amount = profile.totalTracked,
                        type = AmountType.NEUTRAL,
                        showSign = false,
                        style = AmountTextStyle.copy(fontSize = 24.sp),
                    )
                },
                label = "Total tracked",
            )
        }

        // Account section
        Text(
            text = "ACCOUNT",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, letterSpacing = 1.5.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.padding(
                horizontal = Dimens.spaceL,
                vertical = Dimens.space2XL,
            ),
        )

        SettingsRow(
            icon = Icons.Outlined.Edit,
            label = "Edit UPI ID",
            onClick = { onIntent(ProfileIntent.EditUpiIdClicked) },
        )
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = Dimens.spaceL),
            color = MaterialTheme.colorScheme.outlineVariant,
        )
        SettingsRow(
            icon = Icons.Outlined.CurrencyRupee,
            label = "Currency",
            onClick = null,
            trailing = {
                Text(
                    text = profile.currency,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
        )
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = Dimens.spaceL),
            color = MaterialTheme.colorScheme.outlineVariant,
        )
        SettingsRow(
            icon = Icons.Outlined.Notifications,
            label = "Notifications",
            onClick = null,
            trailing = {
                Switch(checked = true, onCheckedChange = {})
            },
        )

        // Dev tools section
        Text(
            text = "DEV TOOLS",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, letterSpacing = 1.5.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.padding(
                horizontal = Dimens.spaceL,
                vertical = Dimens.space2XL,
            ),
        )

        SettingsRow(
            icon = Icons.Outlined.Palette,
            label = "Design System",
            onClick = { onIntent(ProfileIntent.OpenDesignSystemClicked) },
            iconTint = MaterialTheme.colorScheme.primary,
        )
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = Dimens.spaceL),
            color = MaterialTheme.colorScheme.outlineVariant,
        )
        SettingsRow(
            icon = Icons.Outlined.Science,
            label = "MVI Counter Test",
            onClick = { onIntent(ProfileIntent.OpenCounterClicked) },
            iconTint = MaterialTheme.colorScheme.primary,
        )

        // Danger zone
        Text(
            text = "DANGER ZONE",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, letterSpacing = 1.5.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.padding(
                horizontal = Dimens.spaceL,
                vertical = Dimens.space2XL,
            ),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onIntent(ProfileIntent.SignOutClicked) }
                .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.Logout,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.error,
            )
            Spacer(Modifier.width(Dimens.spaceM))
            Text(
                text = "Sign out",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error,
            )
        }

        Spacer(Modifier.height(80.dp))
    }
}

@Composable
private fun ProfileHeader(profile: Profile) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.background,
                    ),
                ),
            )
            .padding(top = Dimens.space4XL, bottom = Dimens.space2XL),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            MemberAvatar(
                name = profile.fullName,
                index = profile.memberIndex,
                size = AvatarSize.XL,
            )
            Spacer(Modifier.height(Dimens.spaceM))
            Text(
                text = profile.fullName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(Dimens.spaceXS))
            if (profile.upiId != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.spaceXS),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Payment,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = profile.upiId ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else {
                Text(
                    text = "No UPI ID set",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontStyle = FontStyle.Italic,
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    value: @Composable () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.spaceL),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            value()
            Spacer(Modifier.height(Dimens.spaceXS))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    label: String,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    iconTint: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurfaceVariant,
    trailing: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    },
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = iconTint,
        )
        Spacer(Modifier.width(Dimens.spaceM))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
        )
        trailing()
    }
}

@Composable
private fun ProfileLoadingSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.spaceL),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(Dimens.space3XL))
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(shimmerBrush()),
        )
        Spacer(Modifier.height(Dimens.spaceM))
        SkeletonBox(
            modifier = Modifier.fillMaxWidth(0.4f),
            height = 20.dp,
        )
        Spacer(Modifier.height(Dimens.spaceM))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimens.spaceM),
        ) {
            SkeletonBox(modifier = Modifier.weight(1f), height = 60.dp)
            SkeletonBox(modifier = Modifier.weight(1f), height = 60.dp)
        }
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
private fun ProfileLoadingPreview() {
    SplitTripTheme {
        ProfileContent(
            state = ProfileState(isLoading = true),
            onIntent = {},
        )
    }
}

@Preview(showBackground = true, name = "Loaded — with UPI ID")
@Composable
private fun ProfileLoadedPreview() {
    SplitTripTheme {
        ProfileContent(
            state = ProfileState(
                profile = Profile(
                    id = "user_1",
                    fullName = "Rahul ",
                    upiId = "rahul@upi",
                    currency = "INR",
                    memberIndex = 0,
                    totalTrips = 4,
                    totalTracked = 84000.0,
                ),
                isLoading = false,
            ),
            onIntent = {},
        )
    }
}

@Preview(showBackground = true, name = "No UPI ID set")
@Composable
private fun ProfileNoUpiPreview() {
    SplitTripTheme {
        ProfileContent(
            state = ProfileState(
                profile = Profile(
                    id = "user_1",
                    fullName = "Rahul ",
                    upiId = null,
                    currency = "INR",
                    memberIndex = 0,
                    totalTrips = 4,
                    totalTracked = 84000.0,
                ),
                isLoading = false,
            ),
            onIntent = {},
        )
    }
}
