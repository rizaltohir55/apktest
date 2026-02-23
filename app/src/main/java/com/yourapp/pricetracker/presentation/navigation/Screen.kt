package com.yourapp.pricetracker.presentation.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object AssetDetail : Screen("asset_detail/{symbol}") {
        fun createRoute(symbol: String) = "asset_detail/$symbol"
    }
    object Watchlist : Screen("watchlist")
    object Alerts : Screen("alerts")
    object Settings : Screen("settings")
}
