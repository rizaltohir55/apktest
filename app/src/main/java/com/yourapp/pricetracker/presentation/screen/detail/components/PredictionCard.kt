package com.yourapp.pricetracker.presentation.screen.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yourapp.pricetracker.domain.model.PredictionResult
import com.yourapp.pricetracker.domain.model.Signal
import com.yourapp.pricetracker.presentation.theme.PriceDown
import com.yourapp.pricetracker.presentation.theme.PriceNeutral
import com.yourapp.pricetracker.presentation.theme.PriceUp

@Composable
fun PredictionCard(
    prediction: PredictionResult,
    modifier: Modifier = Modifier
) {
    val signalColor = when (prediction.signal) {
        Signal.BUY -> PriceUp
        Signal.SELL -> PriceDown
        Signal.HOLD -> PriceNeutral
    }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("AI Forecast Engine", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            Spacer(Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Signal", style = MaterialTheme.typography.bodySmall, color = PriceNeutral)
                    Box(
                        modifier = Modifier
                            .background(signalColor.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = prediction.signal.name,
                            fontWeight = FontWeight.Bold,
                            color = signalColor
                        )
                    }
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text("Confidence", style = MaterialTheme.typography.bodySmall, color = PriceNeutral)
                    Text(
                        text = "${(prediction.confidence * 100).toInt()}%",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            Spacer(Modifier.height(16.dp))
            Text("Predicted Targets (Next 5 periods)", style = MaterialTheme.typography.bodySmall, color = PriceNeutral)
            Spacer(Modifier.height(8.dp))
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                prediction.predictedPrices.take(3).forEachIndexed { index, price ->
                    TargetBox(index + 1, price)
                }
            }
        }
    }
}

@Composable
private fun TargetBox(targetNum: Int, price: Double) {
    Column(
        modifier = Modifier
            .background(Color.Black.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Text("TP$targetNum", style = MaterialTheme.typography.bodySmall, color = PriceNeutral)
        Text("$${String.format("%.2f", price)}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
    }
}
