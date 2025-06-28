package moe.lasoleil.axochat4j.client;

import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.nio.ByteBuffer;

public interface WebSocketConnectionEvent {
    
    @Value
    class Connected implements WebSocketConnectionEvent {
        @NotNull URL url;
        long timestamp;
    }

    @Value
    class Disconnected implements WebSocketConnectionEvent {
        @NotNull URL url;
        int code;
        @Nullable String reason;
        long timestamp;
    }

    @Value
    class TextFrameReceived implements WebSocketConnectionEvent {
        @NotNull String raw;
        long timestamp;
    }

    @Value
    class BinaryFrameReceived implements WebSocketConnectionEvent {
        @NotNull ByteBuffer raw;
        long timestamp;
    }

    @Value
    class ErrorOccurred implements WebSocketConnectionEvent {
        @NotNull Throwable cause;
        long timestamp;
    }

    @Value
    class UnknownMessageFormat implements WebSocketConnectionEvent {
        @NotNull String raw;
        @NotNull Throwable cause;
    }

}
