package com.yourapp.pricetracker.domain.repository

import com.yourapp.pricetracker.domain.model.Alert

interface IAlertRepository {
    suspend fun getActiveAlerts(symbol: String): List<Alert>
    suspend fun deactivateAlert(alertId: String)
}
