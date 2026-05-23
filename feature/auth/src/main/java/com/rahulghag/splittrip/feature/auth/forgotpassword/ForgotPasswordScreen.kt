package com.rahulghag.splittrip.feature.auth.forgotpassword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rahulghag.splittrip.core.ui.components.input.SplitTripTextField
import com.rahulghag.splittrip.core.ui.components.topbar.SplitTripTopBar
import com.rahulghag.splittrip.core.ui.extensions.CollectEvents
import com.rahulghag.splittrip.core.ui.theme.Dimens
import com.rahulghag.splittrip.core.ui.theme.SplitTripTheme

@Composable
fun ForgotPasswordScreen(
    onNavigateUp: () -> Unit,
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CollectEvents(viewModel.uiEvent) { event ->
        when (event) {
            ForgotPasswordEvent.NavigateUp -> onNavigateUp()
        }
    }

    Scaffold(
        topBar = {
            SplitTripTopBar(
                title = "Reset password",
                onNavigateUp = { viewModel.onIntent(ForgotPasswordIntent.NavigateUpClicked) },
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        ForgotPasswordContent(
            state = state,
            onIntent = viewModel::onIntent,
            modifier = Modifier.padding(padding),
        )
    }
}

@Composable
private fun ForgotPasswordContent(
    state: ForgotPasswordState,
    onIntent: (ForgotPasswordIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Dimens.space2XL),
    ) {

        Spacer(Modifier.height(Dimens.space2XL))

        Text(
            text = "Forgot your password?",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.5).sp,
            ),
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(Modifier.height(Dimens.spaceS))

        Text(
            text = "Enter the email address associated with your account and we'll send you a link to reset your password.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(Modifier.height(Dimens.space3XL))

        SplitTripTextField(
            value = state.email,
            onValueChange = { onIntent(ForgotPasswordIntent.EmailChanged(it)) },
            label = "Email",
            placeholder = "you@example.com",
            errorText = state.emailError,
            isError = state.emailError != null,
            enabled = state.successMessage == null,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onIntent(ForgotPasswordIntent.SendResetEmailClicked)
                }
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        if (state.generalError != null) {
            Spacer(Modifier.height(Dimens.spaceM))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(Dimens.spaceM),
            ) {
                Text(
                    text = state.generalError,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }

        if (state.successMessage != null) {
            Spacer(Modifier.height(Dimens.spaceM))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(Dimens.spaceM),
            ) {
                Text(
                    text = state.successMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }

        Spacer(Modifier.height(Dimens.space2XL))

        Button(
            onClick = { onIntent(ForgotPasswordIntent.SendResetEmailClicked) },
            enabled = !state.isLoading && state.successMessage == null,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = MaterialTheme.shapes.medium,
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp,
                )
            } else {
                Text(
                    text = if (state.successMessage != null) "Email sent" else "Send reset email",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ForgotPasswordScreenPreview() {
    SplitTripTheme {
        ForgotPasswordContent(
            state = ForgotPasswordState(),
            onIntent = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ForgotPasswordScreenSuccessPreview() {
    SplitTripTheme {
        ForgotPasswordContent(
            state = ForgotPasswordState(
                email = "user@example.com",
                successMessage = "Reset email sent. Check your inbox.",
            ),
            onIntent = {},
        )
    }
}
