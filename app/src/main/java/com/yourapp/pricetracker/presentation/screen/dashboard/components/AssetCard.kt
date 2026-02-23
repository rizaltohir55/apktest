package com.yourapp.pricetracker.presentation.screen.dashboard.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yourapp.pricetracker.domain.model.Asset
import com.yourapp.pricetracker.domain.model.Price
import com.yourapp.pricetracker.presentation.theme.*

@Composable
fun AssetCard(
    asset: Asset,
    price: Price,
    onClick: () -> Unit
) {
    val priceColor by animateColorAsState(
        targetValue = when {
            price.change > 0 -> PriceUp
            price.change < 0 -> PriceDown
            else -> PriceNeutral
        },
        animationSpec = tween(300),
        label = "priceColorAnimation"
    )
    
    val backgroundColor = remember { Animatable(Color.Transparent) }
    
    LaunchedEffect(price.current) {
        if (price.change > 0) {
            backgroundColor.animateTo(PriceUp.copy(alpha = 0.2f), animationSpec = tween(150))
            backgroundColor.animateTo(Color.Transparent, animationSpec = tween(300))
        } else if (price.change < 0) {
            backgroundColor.animateTo(PriceDown.copy(alpha = 0.2f), animationSpec = tween(150))
            backgroundColor.animateTo(Color.Transparent, animationSpec = tween(300))
        }
    }
    
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                onClick()
            }
            .padding(vertical = 4.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            Modifier
                .padding(16.dp)
                // Remove surface tint to observe the background flush properly or layer it
                , verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(asset.symbol.take(1), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            }
            
            Column(Modifier.weight(1f)) {
                Text(asset.symbol, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                Text(asset.name, style = MaterialTheme.typography.bodySmall, color = PriceNeutral)
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "$", color = MaterialTheme.colorScheme.onSurface)
                    AnimatedCounter(
                        value = price.current,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                
                Text(
                    text = "${if(price.changePercent > 0) "+" else ""}${String.format("%.2f", price.changePercent)}%",
                    color = priceColor,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
