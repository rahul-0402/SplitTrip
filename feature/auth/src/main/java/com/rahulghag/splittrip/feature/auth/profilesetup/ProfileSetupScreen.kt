package com.rahulghag.splittrip.feature.auth.profilesetup

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rahulghag.splittrip.core.ui.components.avatar.AvatarSize
import com.rahulghag.splittrip.core.ui.components.avatar.MemberAvatar
import com.rahulghag.splittrip.core.ui.components.button.SplitTripGhostButton
import com.rahulghag.splittrip.core.ui.components.button.SplitTripPrimaryButton
import com.rahulghag.splittrip.core.ui.components.input.SplitTripTextField
import com.rahulghag.splittrip.core.ui.extensions.CollectEvents
import com.rahulghag.splittrip.core.ui.theme.Dimens
import com.rahulghag.splittrip.core.ui.theme.SplitTripTheme

@Composable
fun ProfileSetupScreen(
    onNavigateToTripList: () -> Unit,
    viewModel: ProfileSetupViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    CollectEvents(viewModel.uiEvent) { event ->
        when (event) {
            ProfileSetupEvent.NavigateToTripList -> onNavigateToTripList()
            is ProfileSetupEvent.ShowSnackbar ->
                snackbarHostState.showSnackbar(event.message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        ProfileSetupContent(
            state = state,
            onIntent = viewModel::onIntent,
            modifier = Modifier.padding(padding),
        )
    }
}

@Composable
private fun ProfileSetupContent(
    state: ProfileSetupState,
    onIntent: (ProfileSetupIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val avatarName = state.fullName.ifBlank { "?" }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Dimens.spaceL),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(Modifier.height(Dimens.space2XL))

        // ── Heading ───────────────────────────
        Text(
            text = "Set up your profile",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(Dimens.spaceS))

        Text(
            text = "Tell us a bit about yourself",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(Dimens.space2XL))

        // ── Avatar ────────────────────────────
        MemberAvatar(
            name = avatarName,
            index = 0,
            size = AvatarSize.XL,
        )

        Spacer(Modifier.height(Dimens.space2XL))

        // ── Form ──────────────────────────────
        SplitTripTextField(
            value = state.fullName,
            onValueChange = { onIntent(ProfileSetupIntent.FullNameChanged(it)) },
            label = "Full name",
            placeholder = "Rahul",
            errorText = state.nameError,
            isError = state.nameError != null,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(Dimens.spaceM))

        SplitTripTextField(
            value = state.upiId,
            onValueChange = { onIntent(ProfileSetupIntent.UpiIdChanged(it)) },
            label = "UPI ID",
            placeholder = "yourname@upi",
            helperText = "Used to generate payment links. You can add this later.",
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(Dimens.spaceM))

        CurrencyInfoRow()

        Spacer(Modifier.height(Dimens.space2XL))

        // ── Actions ───────────────────────────
        SplitTripPrimaryButton(
            text = "Let's go!",
            onClick = { onIntent(ProfileSetupIntent.SaveClicked) },
            isLoading = state.isLoading,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(Dimens.spaceS))

        SplitTripGhostButton(
            text = "Skip for now",
            onClick = { onIntent(ProfileSetupIntent.SkipClicked) },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(Dimens.spaceM))

        Text(
            text = "You can update this anytime from Profile",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(Dimens.space3XL))
    }
}

@Composable
private fun CurrencyInfoRow() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens.spaceXS),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.small)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.small,
                )
                .padding(Dimens.spaceM),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.spaceM),
            ) {
                Text(
                    text = "₹",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "INR — Indian Rupee",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = "Currency locked",
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Text(
            text = "Multi-currency support coming in v2",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = Dimens.spaceS),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileSetupEmptyPreview() {
    SplitTripTheme {
        ProfileSetupContent(
            state = ProfileSetupState(),
            onIntent = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileSetupFilledPreview() {
    SplitTripTheme {
        ProfileSetupContent(
            state = ProfileSetupState(fullName = "Rahul"),
            onIntent = {},
        )
    }
}
