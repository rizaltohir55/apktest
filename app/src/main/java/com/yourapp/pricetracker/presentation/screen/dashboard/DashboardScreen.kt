package com.yourapp.pricetracker.presentation.screen.dashboard

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yourapp.pricetracker.presentation.navigation.Screen
import com.yourapp.pricetracker.presentation.screen.dashboard.components.AssetCard

@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val assets by viewModel.assets.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
    ) {
        items(assets) { (asset, price) ->
            AssetCard(
                asset = asset,
                price = price,
                onClick = {
                    val encodedSymbol = java.net.URLEncoder.encode(asset.symbol, "UTF-8")
                    navController.navigate(Screen.AssetDetail.createRoute(encodedSymbol))
                }
            )
        }
    }
}
