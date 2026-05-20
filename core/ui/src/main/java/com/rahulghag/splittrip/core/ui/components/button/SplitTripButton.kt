package com.rahulghag.splittrip.core.ui.components.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rahulghag.splittrip.core.ui.theme.Dimens
import com.rahulghag.splittrip.core.ui.theme.ErrorRed
import com.rahulghag.splittrip.core.ui.theme.ErrorContainer
import com.rahulghag.splittrip.core.ui.theme.SplitTripTheme

// ── Primary ────────────────────────────────────
@Composable
fun SplitTripPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier.height(50.dp),
        contentPadding = PaddingValues(horizontal = Dimens.space2XL),
        shape = MaterialTheme.shapes.medium,
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp,
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
            )
        }
    }
}

// ── Outline ────────────────────────────────────
@Composable
fun SplitTripOutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier.height(50.dp),
        contentPadding = PaddingValues(horizontal = Dimens.space2XL),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary,
        ),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
            )
        }
    }
}

// ── Ghost / Text ───────────────────────────────
@Composable
fun SplitTripGhostButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    TextButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(50.dp),
        contentPadding = PaddingValues(horizontal = Dimens.spaceL),
        shape = MaterialTheme.shapes.medium,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
        )
    }
}

// ── Danger ─────────────────────────────────────
@Composable
fun SplitTripDangerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(50.dp),
        contentPadding = PaddingValues(horizontal = Dimens.space2XL),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = ErrorContainer,
            contentColor = ErrorRed,
        ),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonsPreview() {
    SplitTripTheme {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement =
                androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
        ) {
            SplitTripPrimaryButton(
                "Add expense",
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )
            SplitTripPrimaryButton(
                "Loading...",
                onClick = {},
                isLoading = true,
                modifier = Modifier.fillMaxWidth()
            )
            SplitTripOutlineButton(
                "Cancel",
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )
            SplitTripGhostButton(
                "Skip for now",
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )
            SplitTripDangerButton(
                "Leave trip",
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
