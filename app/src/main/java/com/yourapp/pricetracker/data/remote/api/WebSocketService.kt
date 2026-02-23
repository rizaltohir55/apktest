package com.yourapp.pricetracker.data.remote.api

import com.yourapp.pricetracker.data.remote.dto.BinanceStreamPayload
import com.yourapp.pricetracker.data.remote.dto.TickerDto
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import timber.log.Timber
import javax.inject.Inject

class WebSocketService @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val json: Json
) {
    private val _priceFlow = MutableSharedFlow<TickerDto>(extraBufferCapacity = 100)
    val priceFlow: SharedFlow<TickerDto> = _priceFlow.asSharedFlow()

    fun connect(symbols: List<String>): Flow<WebSocketState> = callbackFlow {
        val streams = symbols.joinToString("/") { "${it.lowercase()}@ticker" }
        val request = Request.Builder()
            .url("wss://stream.binance.com:9443/stream?streams=$streams")
            .build()
        
        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Timber.d("WebSocket Connected")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val payload = json.decodeFromString<BinanceStreamPayload>(text)
                    _priceFlow.tryEmit(payload.data)
                    trySend(WebSocketState.Connected(payload.data))
                } catch (e: Exception) {
                    Timber.e(e, "Error parsing websocket message")
                    trySend(WebSocketState.Error(e))
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Timber.e(t, "WebSocket Failure")
                trySend(WebSocketState.Error(t))
                // In production, implement auto-reconnect with exponential backoff here
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Timber.d("WebSocket Closed: $reason")
                trySend(WebSocketState.Disconnected)
            }
        }

        val ws = okHttpClient.newWebSocket(request, listener)
        
        awaitClose {
            ws.close(1000, "Flow Closed")
        }
    }
}
