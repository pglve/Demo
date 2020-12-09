/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.websocket

import okhttp3.*
import okio.ByteString

class WebSocketConnection : WebSocketListener() {

    private var webSocket: WebSocket? = null
    private var wsUrl: String = ""
    var statusChangedListener: ((WebSocketStatus) -> Unit)? = null


    companion object {
        val instance: WebSocketConnection by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            WebSocketConnection()
        }
    }

    fun connect() {
        if (webSocket == null) {
            val request = Request.Builder().url(wsUrl).build()
            val client = OkHttpClient.Builder().build()
            webSocket = client.newWebSocket(request, this)
            statusChangedListener?.invoke(WebSocketStatus.Connecting)
        }
    }

    fun cancel() {
        webSocket?.cancel()
    }

    fun close() {
        webSocket?.close(1000, null)
    }

    fun send(value: ByteString) {
        webSocket?.send(value)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        statusChangedListener?.invoke(WebSocketStatus.Closed)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        statusChangedListener?.invoke(WebSocketStatus.Closing)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        statusChangedListener?.invoke(WebSocketStatus.Canceled)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        statusChangedListener?.invoke(WebSocketStatus.Open)
    }
}