package com.rahulghag.splittrip.placeholder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SplashPlaceholder() = Placeholder("Splash")

@Composable
fun LoginPlaceholder() = Placeholder("Login")

@Composable
fun ProfileSetupPlaceholder() = Placeholder("Profile Setup")

@Composable
fun TripListPlaceholder() = Placeholder("Trip List")

@Composable
fun TripDetailPlaceholder(tripId: String) =
    Placeholder("Trip Detail\ntripId: $tripId")

@Composable
fun AddExpensePlaceholder(tripId: String) =
    Placeholder("Add Expense\ntripId: $tripId")

@Composable
fun ExpenseDetailPlaceholder(expenseId: String) =
    Placeholder("Expense Detail\nexpenseId: $expenseId")

@Composable
fun BalancesPlaceholder(tripId: String) =
    Placeholder("Balances\ntripId: $tripId")

@Composable
fun SettleUpPlaceholder(tripId: String) =
    Placeholder("Settle Up\ntripId: $tripId")

@Composable
fun StatsPlaceholder(tripId: String) =
    Placeholder("Stats\ntripId: $tripId")

@Composable
fun ActivityPlaceholder() = Placeholder("Activity")

@Composable
fun ProfilePlaceholder(
    onOpenDesignSystem: () -> Unit = {},
    onOpenCounter: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement
            .spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = "DEV TOOLS",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        com.rahulghag.splittrip.core.ui.components.button
            .SplitTripOutlineButton(
                text = "🎨 Design System",
                onClick = onOpenDesignSystem,
                modifier = Modifier.fillMaxWidth(),
            )
        com.rahulghag.splittrip.core.ui.components.button
            .SplitTripOutlineButton(
                text = "🧪 MVI Counter Test",
                onClick = onOpenCounter,
                modifier = Modifier.fillMaxWidth(),
            )
    }
}

@Composable
fun DesignSystemPlaceholder() = Placeholder("Design System")

@Composable
private fun Placeholder(name: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
