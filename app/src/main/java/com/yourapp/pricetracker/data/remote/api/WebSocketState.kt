package com.yourapp.pricetracker.data.remote.api

import com.yourapp.pricetracker.data.remote.dto.TickerDto

sealed class WebSocketState {
    object Connecting : WebSocketState()
    data class Connected(val ticker: TickerDto) : WebSocketState()
    data class Error(val error: Throwable) : WebSocketState()
    object Disconnected : WebSocketState()
}
