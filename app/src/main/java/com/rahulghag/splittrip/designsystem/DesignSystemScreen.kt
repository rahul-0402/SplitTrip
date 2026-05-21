package com.rahulghag.splittrip.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.outlined.AirplaneTicket
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rahulghag.splittrip.core.ui.components.avatar.AvatarSize
import com.rahulghag.splittrip.core.ui.components.avatar.AvatarStack
import com.rahulghag.splittrip.core.ui.components.avatar.MemberAvatar
import com.rahulghag.splittrip.core.ui.components.button.SplitTripDangerButton
import com.rahulghag.splittrip.core.ui.components.button.SplitTripGhostButton
import com.rahulghag.splittrip.core.ui.components.button.SplitTripOutlineButton
import com.rahulghag.splittrip.core.ui.components.button.SplitTripPrimaryButton
import com.rahulghag.splittrip.core.ui.components.empty.EmptyState
import com.rahulghag.splittrip.core.ui.components.input.SplitTripTextField
import com.rahulghag.splittrip.core.ui.components.loading.SkeletonExpenseRow
import com.rahulghag.splittrip.core.ui.components.loading.SkeletonTripRow
import com.rahulghag.splittrip.core.ui.components.text.AmountText
import com.rahulghag.splittrip.core.ui.components.text.AmountType
import com.rahulghag.splittrip.core.ui.components.topbar.SplitTripTopBar
import com.rahulghag.splittrip.core.ui.theme.AmountTextStyle
import com.rahulghag.splittrip.core.ui.theme.Dimens
import com.rahulghag.splittrip.core.ui.theme.MemberColors
import com.rahulghag.splittrip.core.ui.theme.MemberContainerColors
import com.rahulghag.splittrip.core.ui.theme.SplitTripTheme
import com.rahulghag.splittrip.core.ui.theme.extendedColors

@Composable
fun DesignSystemScreen(
    onNavigateUp: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            SplitTripTopBar(
                title = "Design System",
                subtitle = "Dev only — remove before release",
                onNavigateUp = onNavigateUp,
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {

            // ── Colors ────────────────────────
            DsSection(title = "Brand Colors") {
                DsColorRow(
                    items = listOf(
                        "Primary"   to MaterialTheme.colorScheme.primary,
                        "Container" to MaterialTheme.colorScheme.primaryContainer,
                        "On Primary" to MaterialTheme.colorScheme.onPrimary,
                        "On Container" to MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                )
            }

            DsSection(title = "Surface Colors") {
                DsColorRow(
                    items = listOf(
                        "Background"  to MaterialTheme.colorScheme.background,
                        "Surface"     to MaterialTheme.colorScheme.surface,
                        "SurfaceVar"  to MaterialTheme.colorScheme.surfaceVariant,
                        "Outline"     to MaterialTheme.colorScheme.outline,
                    )
                )
            }

            DsSection(title = "Semantic Colors") {
                DsColorRow(
                    items = listOf(
                        "Success"    to MaterialTheme.extendedColors.success,
                        "Suc. Cont"  to MaterialTheme.extendedColors.successContainer,
                        "Warning"    to MaterialTheme.extendedColors.warning,
                        "War. Cont"  to MaterialTheme.extendedColors.warningContainer,
                        "Error"      to MaterialTheme.colorScheme.error,
                        "Err. Cont"  to MaterialTheme.colorScheme.errorContainer,
                        "Info"       to MaterialTheme.extendedColors.info,
                        "Inf. Cont"  to MaterialTheme.extendedColors.infoContainer,
                    )
                )
            }

            DsSection(title = "Member Avatar Palette") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.spaceL),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.spaceS),
                ) {
                    val names = listOf("R", "P", "A", "S", "M", "K")
                    names.forEachIndexed { index, initial ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(Dimens.spaceXS),
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(MemberColors[index]),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = initial,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(MaterialTheme.shapes.small)
                                    .background(MemberContainerColors[index]),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = initial,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MemberColors[index],
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                            Text(
                                text = "${index + 1}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }

            // ── Typography ────────────────────
            DsSection(title = "Typography") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.spaceL),
                    verticalArrangement = Arrangement.spacedBy(Dimens.spaceM),
                ) {
                    DsTypeRow("displayLarge · 57sp ExtraBold",
                        MaterialTheme.typography.displayLarge
                            .copy(fontSize = 28.sp),
                        "SplitTrip")
                    DsTypeRow("headlineLarge · 32sp Bold",
                        MaterialTheme.typography.headlineLarge,
                        "Goa trip 2025")
                    DsTypeRow("headlineMedium · 24sp Bold",
                        MaterialTheme.typography.headlineMedium,
                        "Add expense")
                    DsTypeRow("titleLarge · 20sp Bold",
                        MaterialTheme.typography.titleLarge,
                        "Dinner at Britto's")
                    DsTypeRow("titleMedium · 16sp SemiBold",
                        MaterialTheme.typography.titleMedium,
                        "Priya paid ₹2,800")
                    DsTypeRow("bodyLarge · 16sp Normal",
                        MaterialTheme.typography.bodyLarge,
                        "Split equally among 4 members")
                    DsTypeRow("bodyMedium · 14sp Normal",
                        MaterialTheme.typography.bodyMedium,
                        "Food · 14 Jan 2025 · paid by Rahul")
                    DsTypeRow("labelLarge · 14sp SemiBold",
                        MaterialTheme.typography.labelLarge,
                        "Add expense")
                    DsTypeRow("labelSmall · 11sp Medium",
                        MaterialTheme.typography.labelSmall,
                        "YOUR SHARE · 0.5sp tracking")
                    DsTypeRow("Amount · JetBrains Mono",
                        AmountTextStyle.copy(fontSize = 20.sp),
                        "+₹18,420.00")
                }
            }

            // ── Shapes ────────────────────────
            DsSection(title = "Shapes & Radius") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.spaceL),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.spaceM),
                ) {
                    listOf(
                        Triple("XS", "4dp", MaterialTheme.shapes.extraSmall),
                        Triple("SM", "8dp", MaterialTheme.shapes.small),
                        Triple("MD", "12dp", MaterialTheme.shapes.medium),
                        Triple("LG", "16dp", MaterialTheme.shapes.large),
                        Triple("XL", "24dp", MaterialTheme.shapes.extraLarge),
                    ).forEach { (name, dp, shape) ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(Dimens.spaceXS),
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(shape)
                                    .background(
                                        MaterialTheme.colorScheme.primaryContainer
                                    )
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.primary
                                            .copy(alpha = 0.3f),
                                        shape,
                                    )
                            )
                            Text(name,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface)
                            Text(dp,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            // ── Spacing ───────────────────────
            DsSection(title = "Spacing Scale") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.spaceL),
                    verticalArrangement = Arrangement.spacedBy(Dimens.spaceXS),
                ) {
                    listOf(
                        Triple("spaceXS",  "4dp",  Dimens.spaceXS),
                        Triple("spaceS",   "8dp",  Dimens.spaceS),
                        Triple("spaceM",   "12dp", Dimens.spaceM),
                        Triple("spaceL",   "16dp", Dimens.spaceL),
                        Triple("spaceXL",  "20dp", Dimens.spaceXL),
                        Triple("space2XL", "24dp", Dimens.space2XL),
                        Triple("space3XL", "32dp", Dimens.space3XL),
                        Triple("space4XL", "48dp", Dimens.space4XL),
                    ).forEach { (name, dp, value) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Dimens.spaceM),
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(value)
                                    .height(14.dp)
                                    .clip(MaterialTheme.shapes.extraSmall)
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                            Text(
                                text = "$name · $dp",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }

            // ── Buttons ───────────────────────
            DsSection(title = "Buttons") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.spaceL),
                    verticalArrangement = Arrangement.spacedBy(Dimens.spaceS),
                ) {
                    SplitTripPrimaryButton(
                        text = "Primary — Add expense",
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                    )
                    SplitTripPrimaryButton(
                        text = "Primary Loading",
                        onClick = {},
                        isLoading = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    SplitTripPrimaryButton(
                        text = "Primary Disabled",
                        onClick = {},
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    SplitTripOutlineButton(
                        text = "Outline — Cancel",
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                    )
                    SplitTripGhostButton(
                        text = "Ghost — Skip for now",
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                    )
                    SplitTripDangerButton(
                        text = "Danger — Leave trip",
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            // ── Text fields ───────────────────
            DsSection(title = "Text Fields") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.spaceL),
                    verticalArrangement = Arrangement.spacedBy(Dimens.spaceM),
                ) {
                    var text1 by remember { mutableStateOf("rahul@gmail.com") }
                    var text2 by remember { mutableStateOf("") }
                    var text3 by remember { mutableStateOf("invalid") }

                    SplitTripTextField(
                        value = text1,
                        onValueChange = { text1 = it },
                        label = "Email — default state",
                    )
                    SplitTripTextField(
                        value = text2,
                        onValueChange = { text2 = it },
                        label = "Password",
                        placeholder = "Enter your password",
                        helperText = "Must be at least 8 characters",
                    )
                    SplitTripTextField(
                        value = text3,
                        onValueChange = { text3 = it },
                        label = "Email — error state",
                        errorText = "Please enter a valid email",
                    )
                }
            }

            // ── Avatars ───────────────────────
            DsSection(title = "Avatars") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.spaceL),
                    verticalArrangement = Arrangement.spacedBy(Dimens.spaceM),
                ) {
                    // All sizes
                    DsLabel("Sizes")
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Dimens.spaceM),
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        AvatarSize.entries.forEachIndexed { i, size ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                MemberAvatar(
                                    name = "Rahul",
                                    index = i,
                                    size = size,
                                )
                                Text(
                                    text = size.name,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                    // Stack
                    DsLabel("Stack with overflow")
                    AvatarStack(
                        names = listOf(
                            "Rahul", "Priya", "Arun", "Sara", "Meera", "Karan"
                        ),
                        maxVisible = 4,
                        size = AvatarSize.MD,
                    )
                }
            }

            // ── Amount text ───────────────────
            DsSection(title = "Amount Text (JetBrains Mono)") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.spaceL),
                    verticalArrangement = Arrangement.spacedBy(Dimens.spaceM),
                ) {
                    DsLabel("Positive — green with + prefix")
                    AmountText(
                        amount = 18420.0,
                        style = AmountTextStyle.copy(fontSize = 28.sp),
                    )
                    DsLabel("Negative — red with - prefix")
                    AmountText(
                        amount = -1200.0,
                        style = AmountTextStyle.copy(fontSize = 28.sp),
                    )
                    DsLabel("Zero / Neutral")
                    AmountText(
                        amount = 0.0,
                        type = AmountType.NEUTRAL,
                        style = AmountTextStyle.copy(fontSize = 28.sp),
                    )
                    DsLabel("With paise")
                    AmountText(
                        amount = 2800.50,
                        style = AmountTextStyle.copy(fontSize = 22.sp),
                    )
                    DsLabel("Large amount — Indian formatting")
                    AmountText(
                        amount = 184200.75,
                        style = AmountTextStyle.copy(fontSize = 22.sp),
                    )
                }
            }

            // ── Skeletons ─────────────────────
            DsSection(title = "Loading Skeletons") {
                Column(modifier = Modifier.fillMaxWidth()) {
                    DsLabel(
                        "Trip row skeleton",
                        modifier = Modifier.padding(
                            horizontal = Dimens.spaceL
                        ),
                    )
                    repeat(2) { SkeletonTripRow() }
                    Spacer(Modifier.height(Dimens.spaceM))
                    DsLabel(
                        "Expense row skeleton",
                        modifier = Modifier.padding(
                            horizontal = Dimens.spaceL
                        ),
                    )
                    repeat(3) { SkeletonExpenseRow() }
                }
            }

            // ── Empty states ──────────────────
            DsSection(title = "Empty States") {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.spaceL),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                ) {
                    EmptyState(
                        icon = Icons.Outlined.AirplaneTicket,
                        title = "No trips yet",
                        subtitle = "Create your first trip and invite" +
                                   " friends to split costs.",
                        actionLabel = "Create trip",
                        onAction = {},
                    )
                }
            }

            // ── MVI Counter (dev test) ────────
            DsSection(title = "MVI Counter (Dev Test)") {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.spaceL),
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 1.dp,
                ) {
                    Text(
                        text = "→ Navigate to Counter via Profile tab",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(Dimens.spaceL),
                    )
                }
            }

            Spacer(Modifier.height(Dimens.space4XL))
        }
    }
}

// ── Helper composables ─────────────────────────

@Composable
private fun DsSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens.spaceM),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(
                    horizontal = Dimens.spaceL,
                    vertical = Dimens.spaceS,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.spaceS),
        ) {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
            )
        }
        content()
        Spacer(Modifier.height(Dimens.spaceS))
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DsColorRow(items: List<Pair<String, Color>>) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.spaceL),
        horizontalArrangement = Arrangement.spacedBy(Dimens.spaceM),
        verticalArrangement = Arrangement.spacedBy(Dimens.spaceM),
    ) {
        items.forEach { (name, color) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens.spaceXS),
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(color)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outline,
                            MaterialTheme.shapes.small,
                        )
                )
                Text(
                    text = name,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun DsTypeRow(
    spec: String,
    style: TextStyle,
    sample: String,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = spec,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = sample,
            style = style,
            color = MaterialTheme.colorScheme.onSurface,
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant
                .copy(alpha = 0.5f),
        )
    }
}

@Composable
private fun DsLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun DesignSystemScreenPreview() {
    SplitTripTheme {
        DesignSystemScreen()
    }
}
