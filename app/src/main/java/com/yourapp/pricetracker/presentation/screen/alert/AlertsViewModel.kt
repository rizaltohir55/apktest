package com.yourapp.pricetracker.presentation.screen.alert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourapp.pricetracker.data.local.database.dao.AlertDao
import com.yourapp.pricetracker.data.local.database.entity.AlertEntity
import com.yourapp.pricetracker.domain.model.AlertType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlertsViewModel @Inject constructor(
    private val alertDao: AlertDao
) : ViewModel() {

    private val _alerts = MutableStateFlow<List<AlertEntity>>(emptyList())
    val alerts = _alerts.asStateFlow()

    init {
        viewModelScope.launch {
            alertDao.getActiveAlerts().collectLatest {
                _alerts.value = it
            }
        }
    }

    fun addAlert(symbol: String, targetValue: Double, type: AlertType) {
        viewModelScope.launch {
            alertDao.insertAlert(
                AlertEntity(
                    symbol = symbol.uppercase(),
                    targetValue = targetValue,
                    type = type,
                    isActive = true
                )
            )
        }
    }

    fun deactivateAlert(alertId: Int) {
        viewModelScope.launch {
            alertDao.deactivateAlert(alertId)
        }
    }
}
