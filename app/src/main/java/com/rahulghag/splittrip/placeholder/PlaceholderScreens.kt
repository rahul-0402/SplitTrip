package com.rahulghag.splittrip.placeholder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

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
fun ProfilePlaceholder() = Placeholder("Profile")

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
