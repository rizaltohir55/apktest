package com.yourapp.pricetracker.presentation.screen.detail.components

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.yourapp.pricetracker.domain.model.Candle

@Composable
fun CandlestickChart(
    candles: List<Candle>,
    modifier: Modifier = Modifier
) {
    if (candles.isEmpty()) return

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            CandleStickChart(context).apply {
                description.isEnabled = false
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(true)
                setPinchZoom(true)
                setDrawGridBackground(false)
                
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    textColor = Color.WHITE
                }

                axisLeft.apply {
                    setDrawGridLines(true)
                    textColor = Color.WHITE
                }

                axisRight.isEnabled = false
                legend.isEnabled = false
            }
        },
        update = { chart ->
            val entries = candles.mapIndexed { index, candle ->
                CandleEntry(
                    index.toFloat(),
                    candle.high.toFloat(),
                    candle.low.toFloat(),
                    candle.open.toFloat(),
                    candle.close.toFloat()
                )
            }

            val dataSet = CandleDataSet(entries, "Price").apply {
                setDrawIcons(false)
                axisDependency = com.github.mikephil.charting.components.YAxis.AxisDependency.LEFT
                shadowColor = Color.DKGRAY
                shadowWidth = 0.7f
                decreasingColor = Color.RED
                decreasingPaintStyle = Paint.Style.FILL
                increasingColor = Color.rgb(0, 192, 135) // PriceUp Green
                increasingPaintStyle = Paint.Style.FILL
                neutralColor = Color.BLUE
                val isDrawValues = false
                setDrawValues(isDrawValues)
            }

            chart.data = CandleData(dataSet)
            chart.invalidate()
        }
    )
}
