package com.yourapp.pricetracker.presentation.screen.detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yourapp.pricetracker.domain.model.TechnicalIndicators
import com.yourapp.pricetracker.presentation.theme.PriceNeutral

@Composable
fun TechnicalIndicatorPanel(
    indicators: TechnicalIndicators,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text("Technical Indicators", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("RSI (14)", color = PriceNeutral, style = MaterialTheme.typography.bodySmall)
                Text(
                    text = indicators.rsi?.let { String.format("%.2f", it) } ?: "--",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Column {
                Text("MACD", color = PriceNeutral, style = MaterialTheme.typography.bodySmall)
                Text(
                    text = indicators.macd?.histogram?.let { String.format("%.2f", it) } ?: "--",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Column {
                Text("EMA (20)", color = PriceNeutral, style = MaterialTheme.typography.bodySmall)
                Text(
                    text = indicators.ema20?.let { String.format("%.2f", it) } ?: "--",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
