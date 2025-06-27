package moe.lasoleil.axochat4j.client;

import moe.lasoleil.axochat4j.exception.PacketIOException;
import moe.lasoleil.axochat4j.packet.AxochatPacket;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.net.URI;
import java.util.function.BiConsumer;

public interface AxochatClientConnection extends Closeable {

    @FunctionalInterface
    interface Factory {
        @NotNull AxochatClientConnection create(@NotNull Config config);
    }

    void send(@NotNull AxochatPacket.C2S packet) throws PacketIOException;

    final class Config {
        private URI uri = null;
        private BiConsumer<AxochatClientConnection, WebSocketConnectionEvent> webSocketHandler = null;
        private BiConsumer<AxochatClientConnection, AxochatPacket.S2C> packetHandler = null;

        private Config() {}

        private static final Config DEFAULT = new Config();

        public static @NotNull Config create() {
            return create(DEFAULT);
        }

        public static @NotNull Config create(@NotNull Config config) {
            return new Config()
                    .uri(config.uri)
                    .webSocketHandler(config.webSocketHandler)
                    .packetHandler(config.packetHandler);
        }

        public @NotNull Config uri(@NotNull URI uri) {
            this.uri = uri;
            return this;
        }

        public @NotNull Config webSocketHandler(@NotNull BiConsumer<AxochatClientConnection, WebSocketConnectionEvent> webSocketHandler) {
            this.webSocketHandler = webSocketHandler;
            return this;
        }

        public @NotNull Config packetHandler(@NotNull BiConsumer<AxochatClientConnection, AxochatPacket.S2C> packetHandler) {
            this.packetHandler = packetHandler;
            return this;
        }

        public @NotNull AxochatClientConnection connect(@NotNull Factory factory) {
            return factory.create(this);
        }
    }

}
