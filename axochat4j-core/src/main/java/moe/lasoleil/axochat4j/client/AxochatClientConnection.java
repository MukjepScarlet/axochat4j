package moe.lasoleil.axochat4j.client;

import lombok.Builder;
import lombok.Value;
import moe.lasoleil.axochat4j.exception.PacketIOException;
import moe.lasoleil.axochat4j.packet.AxochatPacket;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.net.URL;
import java.util.function.BiConsumer;

public interface AxochatClientConnection extends Closeable {

    @FunctionalInterface
    interface Factory<T> {
        @NotNull AxochatClientConnection create(@NotNull T webSocketFactory, @NotNull Config config);
    }

    void send(@NotNull AxochatPacket.C2S packet) throws PacketIOException;

    @Builder
    @Value
    class Config {
        @NotNull URL url;
        @NotNull AxochatPacket.Adaptor packetAdaptor;
        @NotNull BiConsumer<AxochatClientConnection, WebSocketConnectionEvent> webSocketHandler;
        @NotNull BiConsumer<AxochatClientConnection, AxochatPacket.S2C> packetHandler;
    }

}
