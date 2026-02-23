package com.yourapp.pricetracker.presentation.screen.dashboard.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedCounter(
    value: Double,
    color: Color,
    style: TextStyle,
    modifier: Modifier = Modifier
) {
    val formattedValue = String.format("%.2f", value)
    
    Row(modifier = modifier) {
        formattedValue.forEachIndexed { index, char ->
            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInVertically { height -> height } + fadeIn() with
                                slideOutVertically { height -> -height } + fadeOut()
                    } else {
                        slideInVertically { height -> -height } + fadeIn() with
                                slideOutVertically { height -> height } + fadeOut()
                    }.using(SizeTransform(clip = false))
                },
                label = "charAnimation$index"
            ) { targetChar ->
                Text(
                    text = targetChar.toString(),
                    color = color,
                    style = style,
                    softWrap = false
                )
            }
        }
    }
}
