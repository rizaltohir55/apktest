package com.yourapp.pricetracker.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yourapp.pricetracker.core.util.NotificationHelper
import com.yourapp.pricetracker.domain.repository.IAlertRepository
import com.yourapp.pricetracker.domain.repository.IPriceRepository
import com.yourapp.pricetracker.domain.model.AlertType
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class PriceSyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val priceRepository: IPriceRepository,
    private val alertRepository: IAlertRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val alerts = alertRepository.getActiveAlerts().first()
            if (alerts.isEmpty()) return Result.success()

            alerts.groupBy { it.symbol }.forEach { (symbol, symbolAlerts) ->
                val currentPriceResponse = priceRepository.fetchLatestPrice(symbol)
                val currentPrice = currentPriceResponse.current
                
                if (currentPrice > 0.0) {
                    symbolAlerts.forEach { alert ->
                        var isTriggered = false
                        when (alert.type) {
                            AlertType.PRICE_ABOVE -> if (currentPrice >= alert.targetValue) isTriggered = true
                            AlertType.PRICE_BELOW -> if (currentPrice <= alert.targetValue) isTriggered = true
                        }

                        if (isTriggered) {
                            NotificationHelper.showNotification(
                                context = context,
                                title = "Price Alert: $symbol",
                                message = "$symbol has reached $currentPrice (Target: ${alert.targetValue})",
                                notificationId = alert.id.hashCode()
                            )
                            alertRepository.deactivateAlert(alert.id.toInt())
                        }
                    }
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
