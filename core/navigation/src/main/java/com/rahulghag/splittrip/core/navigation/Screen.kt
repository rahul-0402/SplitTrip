package com.rahulghag.splittrip.core.navigation

import kotlinx.serialization.Serializable

/**
 * Type-safe navigation routes using @Serializable.
 *
 * Why @Serializable instead of string routes?
 *   - Compile-time safety — wrong argument types
 *     are caught at build time not runtime
 *   - No string typos — "trip_detail/{tripId}"
 *     becomes TripDetail(tripId = "abc")
 *   - Refactoring is safe — IDE finds all usages
 *   - Arguments are strongly typed
 *
 * Objects = no arguments (singleton destinations)
 * Data classes = have arguments (parameterized destinations)
 */
sealed interface Screen {

    // ── Auth flow ──────────────────────────────
    @Serializable
    data object Splash : Screen

    @Serializable
    data object Login : Screen

    @Serializable
    data object SignUp : Screen

    @Serializable
    data object ForgotPassword : Screen

    @Serializable
    data object ProfileSetup : Screen

    // ── Main flow ──────────────────────────────
    @Serializable
    data object TripList : Screen

    @Serializable
    data class TripDetail(
        val tripId: String,
    ) : Screen

    // ── Expense flow ───────────────────────────
    @Serializable
    data class AddExpense(
        val tripId: String,
    ) : Screen

    @Serializable
    data class ExpenseDetail(
        val expenseId: String,
    ) : Screen

    // ── Trip members ──────────────────────────
    @Serializable
    data class TripMembers(
        val tripId: String,
        val tripName: String,
    ) : Screen

    // ── Settle flow ────────────────────────────
    @Serializable
    data class Balances(
        val tripId: String,
        val tripName: String,
    ) : Screen

    @Serializable
    data class SettleUp(
        val tripId: String,
        val tripName: String,
    ) : Screen

    // ── Stats ──────────────────────────────────
    @Serializable
    data class Stats(
        val tripId: String,
    ) : Screen

    // ── Bottom nav tabs ────────────────────────
    // These have no arguments — they are top-level destinations
    @Serializable
    data object Activity : Screen

    @Serializable
    data object Profile : Screen

    // ── Dev tools ─────────────────────────────
    @Serializable
    data object DesignSystem : Screen

    @Serializable
    data object Counter : Screen
}
