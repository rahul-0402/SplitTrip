package com.rahulghag.splittrip.core.ui.components.empty

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rahulghag.splittrip.core.ui.components.button.SplitTripPrimaryButton
import com.rahulghag.splittrip.core.ui.theme.Dimens
import com.rahulghag.splittrip.core.ui.theme.SplitTripTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AirplaneTicket

@Composable
fun EmptyState(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.space3XL),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
                .copy(alpha = 0.4f),
        )
        Spacer(Modifier.height(Dimens.space2XL))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(Dimens.spaceS))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        if (actionLabel != null && onAction != null) {
            Spacer(Modifier.height(Dimens.space2XL))
            SplitTripPrimaryButton(
                text = actionLabel,
                onClick = onAction,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyStatePreview() {
    SplitTripTheme {
        EmptyState(
            icon = Icons.Outlined.AirplaneTicket,
            title = "No trips yet",
            subtitle = "Create your first trip and invite friends to split costs.",
            actionLabel = "Create trip",
            onAction = {},
        )
    }
}
