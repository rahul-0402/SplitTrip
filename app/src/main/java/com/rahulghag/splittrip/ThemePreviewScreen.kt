package com.rahulghag.splittrip

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rahulghag.splittrip.core.ui.theme.AmountTextStyle
import com.rahulghag.splittrip.core.ui.theme.Dimens
import com.rahulghag.splittrip.core.ui.theme.MemberColors
import com.rahulghag.splittrip.core.ui.theme.SplitTripTheme
import com.rahulghag.splittrip.core.ui.theme.extendedColors

@Composable
fun ThemePreviewScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(Dimens.spaceL)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Dimens.space2XL),
        ) {
            // App name
            Text(
                text = "SplitTrip",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = "Design system preview",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            HorizontalDivider()

            // Typography
            Text(
                text = "TYPOGRAPHY",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text("Display Large — ₹18,420",
                style = MaterialTheme.typography.displayLarge
                    .copy(fontSize = 32.sp))
            Text("Headline Large",
                style = MaterialTheme.typography.headlineLarge)
            Text("Title Large",
                style = MaterialTheme.typography.titleLarge)
            Text("Body Large — regular text",
                style = MaterialTheme.typography.bodyLarge)
            Text("Label Small — CAPS",
                style = MaterialTheme.typography.labelSmall)
            Text(
                text = "+₹18,420.00",
                style = AmountTextStyle.copy(
                    fontSize = 24.sp,
                    color = MaterialTheme.extendedColors.success,
                ),
            )
            Text(
                text = "-₹1,200.00",
                style = AmountTextStyle.copy(
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.error,
                ),
            )

            HorizontalDivider()

            // Colors
            Text(
                text = "BRAND COLORS",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.spaceS)) {
                listOf(
                    Pair("Primary", MaterialTheme.colorScheme.primary),
                    Pair("Container", MaterialTheme.colorScheme.primaryContainer),
                    Pair("Success", MaterialTheme.extendedColors.success),
                    Pair("Warning", MaterialTheme.extendedColors.warning),
                    Pair("Error", MaterialTheme.colorScheme.error),
                ).forEach { (label, color) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(MaterialTheme.shapes.small)
                                .background(color)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            HorizontalDivider()

            // Member avatars
            Text(
                text = "MEMBER AVATAR PALETTE",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.spaceS)) {
                listOf("R", "P", "A", "S", "M", "K").forEachIndexed { index, initial ->
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MemberColors[index % MemberColors.size]),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = initial,
                            style = MaterialTheme.typography.labelLarge,
                            color = androidx.compose.ui.graphics.Color.White,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }

            HorizontalDivider()

            // Shapes
            Text(
                text = "SHAPES",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.spaceS)) {
                listOf(
                    Pair("XS·4dp", MaterialTheme.shapes.extraSmall),
                    Pair("SM·8dp", MaterialTheme.shapes.small),
                    Pair("MD·12dp", MaterialTheme.shapes.medium),
                    Pair("LG·16dp", MaterialTheme.shapes.large),
                    Pair("XL·24dp", MaterialTheme.shapes.extraLarge),
                ).forEach { (label, shape) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(shape)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            Spacer(Modifier.height(Dimens.space4XL))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ThemePreviewScreenPreview() {
    SplitTripTheme {
        ThemePreviewScreen()
    }
}
