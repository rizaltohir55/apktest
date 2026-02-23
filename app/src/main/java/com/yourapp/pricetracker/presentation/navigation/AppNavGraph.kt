package com.yourapp.pricetracker.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

import com.yourapp.pricetracker.presentation.screen.alert.AlertsScreen
import com.yourapp.pricetracker.presentation.screen.dashboard.DashboardScreen
import com.yourapp.pricetracker.presentation.screen.detail.AssetDetailScreen
import com.yourapp.pricetracker.presentation.screen.watchlist.WatchlistScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        composable(route = Screen.Dashboard.route) {
            DashboardScreen(navController = navController)
        }
        composable(
            route = Screen.AssetDetail.route,
            arguments = listOf(
                navArgument("symbol") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            // Route passes to ViewModel via SavedStateHandle, so just call standard composable
            AssetDetailScreen(navController = navController)
        }
        composable(route = Screen.Watchlist.route) {
            WatchlistScreen(navController = navController)
        }
        composable(route = Screen.Alerts.route) {
            AlertsScreen()
        }
        composable(route = Screen.Settings.route) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Settings Screen")
            }
        }
    }
}
