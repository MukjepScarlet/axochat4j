package moe.lasoleil.axochat4j.client;

import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.net.URI;

public interface WebSocketConnectionEvent {
    
    @Value
    class Connected implements WebSocketConnectionEvent {
        @NotNull URI uri;
        long timestamp;
    }

    @Value
    class Disconnected implements WebSocketConnectionEvent {
        @NotNull URI uri;
        int code;
        @NotNull String reason;
        long timestamp;
    }

    @Value
    class TextFrameReceived implements WebSocketConnectionEvent {
        @NotNull String raw;
        long timestamp;
    }

    @Value
    class TextFrameSent implements WebSocketConnectionEvent {
        @NotNull String raw;
        long timestamp;
    }

    @Value
    class ErrorOccurred implements WebSocketConnectionEvent {
        @NotNull Throwable cause;
        long timestamp;
    }

    @Value
    class UnknownMessageFormat implements WebSocketConnectionEvent {
        @NotNull String message;
        @NotNull Exception cause;
    }

}
