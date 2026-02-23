package com.yourapp.pricetracker.data.remote.repository

import com.yourapp.pricetracker.data.local.database.dao.AlertDao
import com.yourapp.pricetracker.domain.model.Alert
import com.yourapp.pricetracker.domain.repository.IAlertRepository
import javax.inject.Inject

class AlertRepositoryImpl @Inject constructor(
    private val alertDao: AlertDao
) : IAlertRepository {
    override suspend fun getActiveAlerts(symbol: String): List<Alert> {
        return alertDao.getActiveAlerts(symbol).map { Alert(it.id, it.symbol, false) }
    }

    override suspend fun deactivateAlert(alertId: String) {
        // Placeholder
    }
}
