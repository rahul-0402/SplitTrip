package com.rahulghag.splittrip.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.rahulghag.splittrip.core.navigation.Screen
import com.rahulghag.splittrip.designsystem.DesignSystemScreen
import com.rahulghag.splittrip.feature.auth.login.LoginScreen
import com.rahulghag.splittrip.feature.auth.profilesetup.ProfileSetupScreen
import com.rahulghag.splittrip.feature.auth.splash.SplashScreen
import com.rahulghag.splittrip.feature.activity.ActivityScreen
import com.rahulghag.splittrip.feature.profile.ProfileScreen
import com.rahulghag.splittrip.feature.trips.tripmembers.TripMembersScreen
import com.rahulghag.splittrip.feature.settle.balances.BalancesScreen
import com.rahulghag.splittrip.feature.settle.settleup.SettleUpScreen
import com.rahulghag.splittrip.feature.trips.addexpense.AddExpenseScreen
import com.rahulghag.splittrip.feature.trips.expensedetail.ExpenseDetailScreen
import com.rahulghag.splittrip.feature.trips.tripdetail.TripDetailScreen
import com.rahulghag.splittrip.feature.trips.triplist.TripListScreen
import com.rahulghag.splittrip.placeholder.StatsPlaceholder

@Composable
fun SplitTripNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: Screen = Screen.Splash,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {

        // ── Auth ──────────────────────────────
        composable<Screen.Splash> {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login) {
                        popUpTo(Screen.Splash) { inclusive = true }
                    }
                },
                onNavigateToTripList = {
                    navController.navigate(Screen.TripList) {
                        popUpTo(Screen.Splash) { inclusive = true }
                    }
                },
            )
        }

        composable<Screen.Login> {
            LoginScreen(
                onNavigateToTripList = {
                    navController.navigate(Screen.TripList) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                },
                onNavigateToProfileSetup = {
                    navController.navigate(Screen.ProfileSetup) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    // TODO: SignUp screen later
                },
                onNavigateToForgotPassword = {
                    // TODO: ForgotPassword screen later
                },
            )
        }

        composable<Screen.ProfileSetup> {
            ProfileSetupScreen(
                onNavigateToTripList = {
                    navController.navigate(Screen.TripList) {
                        popUpTo(Screen.ProfileSetup) { inclusive = true }
                    }
                },
            )
        }

        // ── Main ──────────────────────────────
        composable<Screen.TripList> {
            TripListScreen(
                onNavigateToTripDetail = { tripId ->
                    navController.navigate(Screen.TripDetail(tripId))
                },
            )
        }

        composable<Screen.TripDetail> {
            TripDetailScreen(
                onNavigateUp = {
                    navController.navigateUp()
                },
                onNavigateToExpenseDetail = { expenseId ->
                    navController.navigate(Screen.ExpenseDetail(expenseId))
                },
                onNavigateToAddExpense = { tripId ->
                    navController.navigate(Screen.AddExpense(tripId))
                },
                onNavigateToBalances = { tripId, tripName ->
                    navController.navigate(Screen.Balances(tripId, tripName))
                },
                onNavigateToTripMembers = { tripId, tripName ->
                    navController.navigate(Screen.TripMembers(tripId, tripName))
                },
            )
        }

        composable<Screen.TripMembers> {
            TripMembersScreen(
                onNavigateUp = { navController.navigateUp() },
            )
        }

        // ── Expenses ──────────────────────────
        composable<Screen.AddExpense> {
            AddExpenseScreen(
                onNavigateUp = { navController.navigateUp() },
            )
        }

        composable<Screen.ExpenseDetail> {
            ExpenseDetailScreen(
                onNavigateUp = { navController.navigateUp() },
                onNavigateToEdit = { /* TODO: EditExpense screen later */ },
            )
        }

        // ── Settle ────────────────────────────
        composable<Screen.Balances> {
            BalancesScreen(
                onNavigateUp = { navController.navigateUp() },
                onNavigateToSettleUp = { tripId, tripName ->
                    navController.navigate(Screen.SettleUp(tripId, tripName))
                },
            )
        }

        composable<Screen.SettleUp> {
            SettleUpScreen(
                onNavigateUp = { navController.navigateUp() },
            )
        }

        // ── Stats ─────────────────────────────
        composable<Screen.Stats> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.Stats>()
            StatsPlaceholder(tripId = route.tripId)
        }

        // ── Bottom nav tabs ───────────────────
        composable<Screen.Activity> {
            ActivityScreen(
                onNavigateToTripDetail = { tripId ->
                    navController.navigate(Screen.TripDetail(tripId))
                },
            )
        }

        composable<Screen.Profile> {
            ProfileScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToDesignSystem = {
                    navController.navigate(Screen.DesignSystem)
                },
                onNavigateToCounter = {
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
