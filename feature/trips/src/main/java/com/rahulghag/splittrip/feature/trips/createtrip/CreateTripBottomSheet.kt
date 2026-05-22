package com.rahulghag.splittrip.feature.trips.createtrip

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rahulghag.splittrip.core.ui.components.button.SplitTripPrimaryButton
import com.rahulghag.splittrip.core.ui.components.input.SplitTripTextField
import com.rahulghag.splittrip.core.ui.extensions.CollectEvents
import com.rahulghag.splittrip.core.ui.theme.Dimens
import com.rahulghag.splittrip.core.ui.theme.SplitTripTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTripBottomSheet(
    onDismiss: () -> Unit,
    onTripCreated: (String) -> Unit,
    viewModel: CreateTripViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val onIntent = viewModel::onIntent

    CollectEvents(viewModel.uiEvent) { event ->
        when (event) {
            CreateTripEvent.Dismiss -> onDismiss()
            is CreateTripEvent.NavigateToTripDetail -> onTripCreated(event.tripId)
            is CreateTripEvent.ShowSnackbar -> Unit
        }
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = { onIntent(CreateTripIntent.DismissClicked) },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // Handle bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.spaceM),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 32.dp, height = 4.dp)
                        .background(
                            MaterialTheme.colorScheme.outlineVariant,
                            MaterialTheme.shapes.extraSmall,
                        ),
                )
            }

            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "New trip",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = { onIntent(CreateTripIntent.DismissClicked) }) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Dismiss",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // Icon picker row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Icon",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                )
                Row(
                    modifier = Modifier.clickable { onIntent(CreateTripIntent.ToggleIconPicker) },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.spaceS),
                ) {
                    Text(text = state.selectedIcon, fontSize = 28.sp)
                    Icon(
                        imageVector = Icons.Outlined.ExpandMore,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            if (state.isIconPickerOpen) {
                IconPickerGrid(
                    icons = viewModel.availableIcons,
                    selectedIcon = state.selectedIcon,
                    onIconSelected = { onIntent(CreateTripIntent.IconSelected(it)) },
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // Trip name field
            SplitTripTextField(
                value = state.tripName,
                onValueChange = { onIntent(CreateTripIntent.TripNameChanged(it)) },
                label = "Trip name",
                placeholder = "Goa trip 2025",
                leadingIcon = {
                    Text(text = state.selectedIcon, fontSize = 20.sp)
                },
                errorText = state.tripNameError,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM),
            )

            // Date picker row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM),
                horizontalArrangement = Arrangement.spacedBy(Dimens.spaceM),
            ) {
                DateField(
                    label = "FROM",
                    date = state.startDate,
                    onClick = { onIntent(CreateTripIntent.OpenStartDatePicker) },
                    modifier = Modifier.weight(1f),
                )
                DateField(
                    label = "TO",
                    date = state.endDate,
                    onClick = { onIntent(CreateTripIntent.OpenEndDatePicker) },
                    modifier = Modifier.weight(1f),
                )
            }

            val dateError = state.dateError
            if (dateError != null) {
                Text(
                    text = dateError,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = Dimens.spaceL),
                )
            }

            // Currency row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Currency",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.spaceS),
                ) {
                    Text(
                        text = "₹",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(text = "INR", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = "· v2",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            // Create button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM)
                    .navigationBarsPadding(),
            ) {
                SplitTripPrimaryButton(
                    text = "Create trip",
                    isLoading = state.isLoading,
                    onClick = { onIntent(CreateTripIntent.CreateClicked) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            // Date pickers
            if (state.isStartDatePickerOpen) {
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = state.startDate?.toEpochMillis(),
                )
                DatePickerDialog(
                    onDismissRequest = { onIntent(CreateTripIntent.DismissStartDatePicker) },
                    confirmButton = {
                        TextButton(onClick = {
                            val millis = datePickerState.selectedDateMillis
                            if (millis != null) {
                                onIntent(CreateTripIntent.StartDateSelected(millisToDateString(millis)))
                            } else {
                                onIntent(CreateTripIntent.DismissStartDatePicker)
                            }
                        }) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            onIntent(CreateTripIntent.DismissStartDatePicker)
                        }) { Text("Cancel") }
                    },
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            if (state.isEndDatePickerOpen) {
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = state.endDate?.toEpochMillis(),
                )
                DatePickerDialog(
                    onDismissRequest = { onIntent(CreateTripIntent.DismissEndDatePicker) },
                    confirmButton = {
                        TextButton(onClick = {
                            val millis = datePickerState.selectedDateMillis
                            if (millis != null) {
                                onIntent(CreateTripIntent.EndDateSelected(millisToDateString(millis)))
                            } else {
                                onIntent(CreateTripIntent.DismissEndDatePicker)
                            }
                        }) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            onIntent(CreateTripIntent.DismissEndDatePicker)
                        }) { Text("Cancel") }
                    },
                ) {
                    DatePicker(state = datePickerState)
                }
            }
        }
    }
}

@Composable
private fun IconPickerGrid(
    icons: List<String>,
    selectedIcon: String,
    onIconSelected: (String) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceS),
        horizontalArrangement = Arrangement.spacedBy(Dimens.spaceS),
        verticalArrangement = Arrangement.spacedBy(Dimens.spaceS),
    ) {
        items(icons) { icon ->
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        color = if (icon == selectedIcon)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.medium,
                    )
                    .clickable { onIconSelected(icon) },
                contentAlignment = Alignment.Center,
            ) {
                Text(text = icon, fontSize = 24.sp)
            }
        }
    }
}

@Composable
private fun DateField(
    label: String,
    date: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.small)
            .clickable { onClick() }
            .padding(Dimens.spaceM),
    ) {
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(2.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.spaceXS),
            ) {
                Text(
                    text = date?.let { formatDateShort(it) } ?: "Select",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (date != null)
                        MaterialTheme.colorScheme.onSurface
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                )
                Icon(
                    imageVector = Icons.Outlined.CalendarToday,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

private fun formatDateShort(date: String): String {
    val parts = date.split("-")
    if (parts.size != 3) return date
    val months = listOf(
        "", "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
    )
    val month = months.getOrElse(parts[1].toIntOrNull() ?: 0) { parts[1] }
    return "${parts[2].toIntOrNull() ?: parts[2]} $month"
}

private fun millisToDateString(millis: Long): String {
    val date = Instant.ofEpochMilli(millis)
        .atZone(ZoneId.of("UTC"))
        .toLocalDate()
    return "%04d-%02d-%02d".format(date.year, date.monthValue, date.dayOfMonth)
}

private fun String.toEpochMillis(): Long? = try {
    val parts = split("-")
    LocalDate.of(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
        .atStartOfDay(ZoneId.of("UTC"))
        .toInstant().toEpochMilli()
} catch (_: Exception) { null }

@Preview(showBackground = true, name = "Default (empty form)")
@Composable
private fun CreateTripEmptyPreview() {
    SplitTripTheme {
        SheetPreviewContent(
            icon = "✈️",
            tripName = "",
            startDate = null,
            endDate = null,
        )
    }
}

@Preview(showBackground = true, name = "Filled — Goa trip with dates")
@Composable
private fun CreateTripFilledPreview() {
    SplitTripTheme {
        SheetPreviewContent(
            icon = "🏖️",
            tripName = "Goa trip 2025",
            startDate = "2025-01-14",
            endDate = "2025-01-18",
        )
    }
}

@Composable
private fun SheetPreviewContent(
    icon: String,
    tripName: String,
    startDate: String?,
    endDate: String?,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimens.spaceM),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(width = 32.dp, height = 4.dp)
                    .background(MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.extraSmall),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "New trip",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f),
            )
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Icon",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
            )
            Text(text = icon, fontSize = 28.sp)
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        SplitTripTextField(
            value = tripName,
            onValueChange = {},
            label = "Trip name",
            placeholder = "Goa trip 2025",
            leadingIcon = { Text(text = icon, fontSize = 20.sp) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM),
            horizontalArrangement = Arrangement.spacedBy(Dimens.spaceM),
        ) {
            DateField(label = "FROM", date = startDate, onClick = {}, modifier = Modifier.weight(1f))
            DateField(label = "TO", date = endDate, onClick = {}, modifier = Modifier.weight(1f))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Currency",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.spaceS),
            ) {
                Text("₹", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                Text("INR", style = MaterialTheme.typography.bodyLarge)
                Text("· v2", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        SplitTripPrimaryButton(
            text = "Create trip",
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM),
        )
    }
}
