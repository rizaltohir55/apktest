package com.yourapp.pricetracker.presentation.screen.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yourapp.pricetracker.presentation.screen.detail.components.CandlestickChart
import com.yourapp.pricetracker.presentation.screen.detail.components.PredictionCard
import com.yourapp.pricetracker.presentation.screen.detail.components.TechnicalIndicatorPanel

@Composable
fun AssetDetailScreen(
    navController: NavController,
    viewModel: AssetDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${state.symbol} Overview",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            state.currentPrice?.let {
                Text(
                    text = "$${String.format("%.2f", it.current)}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        
        Spacer(Modifier.height(16.dp))

        // Chart Area (takes available space)
        Box(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
        ) {
            CandlestickChart(candles = state.candles)
        }

        Spacer(Modifier.height(16.dp))

        // Indicators
        TechnicalIndicatorPanel(
            indicators = state.indicators,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        // Prediction Strategy Card
        state.prediction?.let {
            PredictionCard(prediction = it)
        }
    }
}
