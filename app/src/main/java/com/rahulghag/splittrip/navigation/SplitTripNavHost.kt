package com.rahulghag.splittrip.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.rahulghag.splittrip.core.navigation.Screen
import com.rahulghag.splittrip.designsystem.DesignSystemScreen
import com.rahulghag.splittrip.placeholder.ActivityPlaceholder
import com.rahulghag.splittrip.placeholder.AddExpensePlaceholder
import com.rahulghag.splittrip.placeholder.BalancesPlaceholder
import com.rahulghag.splittrip.placeholder.ExpenseDetailPlaceholder
import com.rahulghag.splittrip.placeholder.LoginPlaceholder
import com.rahulghag.splittrip.placeholder.ProfilePlaceholder
import com.rahulghag.splittrip.placeholder.ProfileSetupPlaceholder
import com.rahulghag.splittrip.placeholder.SettleUpPlaceholder
import com.rahulghag.splittrip.placeholder.SplashPlaceholder
import com.rahulghag.splittrip.placeholder.StatsPlaceholder
import com.rahulghag.splittrip.placeholder.TripDetailPlaceholder
import com.rahulghag.splittrip.placeholder.TripListPlaceholder

@Composable
fun SplitTripNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    // Start at TripList for now — will be Splash once auth is ready
    startDestination: Screen = Screen.TripList,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {

        // ── Auth ──────────────────────────────
        composable<Screen.Splash> {
            SplashPlaceholder()
        }

        composable<Screen.Login> {
            LoginPlaceholder()
        }

        composable<Screen.ProfileSetup> {
            ProfileSetupPlaceholder()
        }

        // ── Main ──────────────────────────────
        composable<Screen.TripList> {
            TripListPlaceholder()
        }

        composable<Screen.TripDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.TripDetail>()
            TripDetailPlaceholder(tripId = route.tripId)
        }

        // ── Expenses ──────────────────────────
        composable<Screen.AddExpense> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.AddExpense>()
            AddExpensePlaceholder(tripId = route.tripId)
        }

        composable<Screen.ExpenseDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.ExpenseDetail>()
            ExpenseDetailPlaceholder(expenseId = route.expenseId)
        }

        // ── Settle ────────────────────────────
        composable<Screen.Balances> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.Balances>()
            BalancesPlaceholder(tripId = route.tripId)
        }

        composable<Screen.SettleUp> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.SettleUp>()
            SettleUpPlaceholder(tripId = route.tripId)
        }

        // ── Stats ─────────────────────────────
        composable<Screen.Stats> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.Stats>()
            StatsPlaceholder(tripId = route.tripId)
        }

        // ── Bottom nav tabs ───────────────────
        composable<Screen.Activity> {
            ActivityPlaceholder()
        }

        composable<Screen.Profile> {
            ProfilePlaceholder(
                onOpenDesignSystem = {
                    navController.navigate(Screen.DesignSystem)
                },
                onOpenCounter = {
                    navController.navigate(Screen.Counter)
                },
            )
        }

        // ── Dev tools ─────────────────────────
        composable<Screen.DesignSystem> {
            DesignSystemScreen(
                onNavigateUp = { navController.navigateUp() },
            )
        }

        composable<Screen.Counter> {
            com.rahulghag.splittrip.test.CounterScreen()
        }
    }
}
