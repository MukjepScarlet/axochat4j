@file:JvmName("OkHttpAxochatClient")
package moe.lasoleil.axochat4j.client

import moe.lasoleil.axochat4j.packet.AxochatPacket
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

object OkHttpAxochatClient : AxochatClientConnection.Factory<WebSocket.Factory> {

    override fun create(
        webSocketFactory: WebSocket.Factory,
        config: AxochatClientConnection.Config
    ): AxochatClientConnection {
        require(config.packetAdaptor.type() == AxochatPacket.Adaptor.Type.CLIENT) {
            "Packet adaptor type must be CLIENT (for C2S sending and S2C receiving)"
        }

        val request = Request.Builder()
            .url(config.url)
            .build()

        var webSocket: WebSocket? = null

        val connection = object : AxochatClientConnection {
            override fun send(packet: AxochatPacket.C2S) {
                val webSocket = webSocket ?: error("WebSocket uninitialized")
                val string = buildString {
                    config.packetAdaptor.write(this, packet)
                }
                webSocket.send(string)
            }

            override fun close() {
                webSocket?.close(1000, null) ?: error("WebSocket uninitialized")
            }
        }

        webSocket = webSocketFactory.newWebSocket(request, object : WebSocketListener() {
            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                runCatching {
                    config.webSocketHandler.accept(
                        connection,
                        WebSocketConnectionEvent.Disconnected(config.url, code, reason, System.currentTimeMillis())
                    )
                }.onFailure { onFailure(webSocket, it, null) }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(code, null)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                config.webSocketHandler.accept(
                    connection,
                    WebSocketConnectionEvent.ErrorOccurred(t, System.currentTimeMillis())
                )
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                runCatching {
                    config.webSocketHandler.accept(
                        connection,
                        WebSocketConnectionEvent.TextFrameReceived(text, System.currentTimeMillis())
                    )
                }.onFailure { onFailure(webSocket, it, null) }

                runCatching {
                    config.packetAdaptor.read(text) as AxochatPacket.S2C
                }.onFailure {
                    config.webSocketHandler.accept(
                        connection,
                        WebSocketConnectionEvent.UnknownMessageFormat(text, it)
                    )
                }.onSuccess {
                    config.packetHandler.accept(connection, it)
                }
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                // Axochat doesn't use binary frames
                runCatching {
                    config.webSocketHandler.accept(
                        connection,
                        WebSocketConnectionEvent.BinaryFrameReceived(bytes.asByteBuffer(), System.currentTimeMillis())
                    )
                }.onFailure { onFailure(webSocket, it, null) }
            }

            override fun onOpen(webSocket: WebSocket, response: Response) {
                runCatching {
                    config.webSocketHandler.accept(
                        connection,
                        WebSocketConnectionEvent.Connected(config.url, System.currentTimeMillis())
                    )
                }.onFailure { onFailure(webSocket, it, null) }
            }
        })

        return connection
    }

    @JvmSynthetic
    @Suppress("NOTHING_TO_INLINE")
    fun WebSocket.Factory.newAxochatConnection(
        config: AxochatClientConnection.Config,
    ): AxochatClientConnection {
        return create(this, config)
    }

}