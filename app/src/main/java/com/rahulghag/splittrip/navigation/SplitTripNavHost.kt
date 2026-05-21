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
import com.rahulghag.splittrip.feature.trips.triplist.TripListScreen
import com.rahulghag.splittrip.placeholder.ActivityPlaceholder
import com.rahulghag.splittrip.placeholder.AddExpensePlaceholder
import com.rahulghag.splittrip.placeholder.BalancesPlaceholder
import com.rahulghag.splittrip.placeholder.CreateTripPlaceholder
import com.rahulghag.splittrip.placeholder.ExpenseDetailPlaceholder
import com.rahulghag.splittrip.placeholder.ProfilePlaceholder
import com.rahulghag.splittrip.placeholder.SettleUpPlaceholder
import com.rahulghag.splittrip.placeholder.StatsPlaceholder
import com.rahulghag.splittrip.placeholder.TripDetailPlaceholder

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
                onNavigateToCreateTrip = {
                    navController.navigate(Screen.CreateTrip)
                },
            )
        }

        composable<Screen.TripDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.TripDetail>()
            TripDetailPlaceholder(tripId = route.tripId)
        }

        composable<Screen.CreateTrip> {
            CreateTripPlaceholder()
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
