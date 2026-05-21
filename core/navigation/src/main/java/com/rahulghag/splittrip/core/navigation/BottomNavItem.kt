package com.rahulghag.splittrip.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Bottom navigation tabs.
 * Each item maps to a Screen destination.
 *
 * selectedIcon — filled variant shown when tab is active
 * unselectedIcon — outlined variant shown when tab is inactive
 */
enum class BottomNavItem(
    val screen: Screen,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    Trips(
        screen = Screen.TripList,
        label = "Trips",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    Activity(
        screen = Screen.Activity,
        label = "Activity",
        selectedIcon = Icons.Filled.Notifications,
        unselectedIcon = Icons.Outlined.Notifications,
    ),
    Profile(
        screen = Screen.Profile,
        label = "Profile",
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle,
    ),
}

// Screens that show bottom nav
val bottomNavScreens = setOf(
    Screen.TripList,
    Screen.Activity,
    Screen.Profile,
)
