package com.yourapp.pricetracker.presentation.screen.alert

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yourapp.pricetracker.domain.model.AlertType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertsScreen(
    viewModel: AlertsViewModel = hiltViewModel()
) {
    val alerts by viewModel.alerts.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Alert")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            Text("Active Alerts", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))

            if (alerts.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No active alerts.")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(alerts) { alert ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(alert.symbol, style = MaterialTheme.typography.titleMedium)
                                    Text(
                                        "${if(alert.type == AlertType.PRICE_ABOVE) "Above" else "Below"} $${alert.targetValue}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                Button(onClick = { viewModel.deactivateAlert(alert.id.toInt()) }) {
                                    Text("Remove")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
        var symbol by remember { mutableStateOf("") }
        var priceStr by remember { mutableStateOf("") }
        var isAbove by remember { mutableStateOf(true) }

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("New Alert") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = symbol,
                        onValueChange = { symbol = it },
                        label = { Text("Symbol (e.g. BTCUSDT)") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = priceStr,
                        onValueChange = { priceStr = it },
                        label = { Text("Target Price") },
                        singleLine = true
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Trigger when price goes:")
                        Spacer(Modifier.weight(1f))
                        TextButton(onClick = { isAbove = !isAbove }) {
                            Text(if (isAbove) "UP (Above)" else "DOWN (Below)")
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    val target = priceStr.toDoubleOrNull()
                    if (symbol.isNotBlank() && target != null) {
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        viewModel.addAlert(
                            symbol = symbol,
                            targetValue = target,
                            type = if (isAbove) AlertType.PRICE_ABOVE else AlertType.PRICE_BELOW
                        )
                        showDialog = false
                    }
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
