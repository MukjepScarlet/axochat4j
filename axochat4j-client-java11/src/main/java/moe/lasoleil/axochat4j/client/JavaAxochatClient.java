package moe.lasoleil.axochat4j.client;

import moe.lasoleil.axochat4j.exception.PacketIOException;
import moe.lasoleil.axochat4j.packet.AxochatPacket;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletionStage;

public final class JavaAxochatClient implements AxochatClientConnection.Factory<WebSocket.Builder> {

    public static final JavaAxochatClient INSTANCE = new JavaAxochatClient();

    private JavaAxochatClient() {}

    @Override
    public @NotNull AxochatClientConnection create(
            WebSocket.@NotNull Builder webSocketFactory,
            AxochatClientConnection.@NotNull Config config
    ) {
        if (config.getPacketAdaptor().type() != AxochatPacket.Adaptor.Type.CLIENT) {
            throw new IllegalArgumentException("Packet adaptor type must be CLIENT");
        }

        URI uri;
        try {
            uri = config.getUrl().toURI();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL", e);
        }

        var webSocketRef = new WebSocket[1];

        var connection = new AxochatClientConnection() {
            @Override
            public void send(@NotNull AxochatPacket.C2S packet) throws PacketIOException {
                var buf = new StringBuilder();
                config.getPacketAdaptor().write(buf, packet);
                webSocketRef[0].sendText(buf.toString(), true);
            }

            @Override
            public void close() {
                webSocketRef[0].sendClose(WebSocket.NORMAL_CLOSURE, null);
            }
        };

        webSocketFactory
                .buildAsync(uri, new WebSocket.Listener() {
                    @Override
                    public void onOpen(WebSocket webSocket) {
                        webSocketRef[0] = webSocket;
                        config.getWebSocketHandler().accept(
                                connection,
                                new WebSocketConnectionEvent.Connected(config.getUrl(), System.currentTimeMillis())
                        );
                        WebSocket.Listener.super.onOpen(webSocket);
                    }

                    @Override
                    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                        String raw = data.toString();
                        try {
                            config.getWebSocketHandler().accept(
                                    connection,
                                    new WebSocketConnectionEvent.TextFrameReceived(raw, System.currentTimeMillis())
                            );
                        } catch (Throwable t) {
                            onError(webSocket, t);
                        }

                        try {
                            AxochatPacket.S2C packet = (AxochatPacket.S2C) config.getPacketAdaptor().read(raw);
                            config.getPacketHandler().accept(connection, packet);
                        } catch (Throwable t) {
                            config.getWebSocketHandler().accept(
                                    connection,
                                    new WebSocketConnectionEvent.UnknownMessageFormat(raw, t)
                            );
                        }

                        return WebSocket.Listener.super.onText(webSocket, data, last);
                    }

                    @Override
                    public CompletionStage<?> onBinary(WebSocket webSocket, ByteBuffer data, boolean last) {
                        try {
                            config.getWebSocketHandler().accept(
                                    connection,
                                    new WebSocketConnectionEvent.BinaryFrameReceived(data, System.currentTimeMillis())
                            );
                        } catch (Throwable t) {
                            onError(webSocket, t);
                        }

                        return WebSocket.Listener.super.onBinary(webSocket, data, last);
                    }

                    @Override
                    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
                        try {
                            config.getWebSocketHandler().accept(
                                    connection,
                                    new WebSocketConnectionEvent.Disconnected(config.getUrl(), statusCode, reason, System.currentTimeMillis())
                            );
                        } catch (Throwable t) {
                            onError(webSocket, t);
                        }
                        return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
                    }

                    @Override
                    public void onError(WebSocket webSocket, Throwable error) {
                        config.getWebSocketHandler().accept(
                                connection,
                                new WebSocketConnectionEvent.ErrorOccurred(error, System.currentTimeMillis())
                        );
                        WebSocket.Listener.super.onError(webSocket, error);
                    }
                });

        return connection;
    }
}