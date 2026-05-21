package com.rahulghag.splittrip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rahulghag.splittrip.core.navigation.Screen
import com.rahulghag.splittrip.core.navigation.bottomNavScreens
import com.rahulghag.splittrip.core.ui.theme.SplitTripTheme
import com.rahulghag.splittrip.navigation.SplitTripBottomBar
import com.rahulghag.splittrip.navigation.SplitTripNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SplitTripTheme {
                val navController = rememberNavController()
                val currentBackStack by
                    navController.currentBackStackEntryAsState()
                val currentDestination = currentBackStack?.destination

                // Show bottom nav only on top-level screens
                val showBottomBar = bottomNavScreens.any { screen ->
                    currentDestination?.hasRoute(screen::class) == true
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomBar) {
                            SplitTripBottomBar(
                                currentDestination = currentDestination,
                                onNavigate = { screen ->
                                    navController.navigate(screen) {
                                        // Pop up to TripList to avoid
                                        // building up a large back stack
                                        popUpTo(Screen.TripList) {
                                            saveState = true
                                        }
                                        // Avoid duplicate destinations
                                        launchSingleTop = true
                                        // Restore state when reselecting a tab
                                        restoreState = true
                                    }
                                },
                            )
                        }
                    },
                ) { innerPadding ->
                    SplitTripNavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}
