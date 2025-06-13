package moe.lasoleil.axochat4j.packet;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * <h2>AXOCHAT PROTOCOL</h2>
 * <a href="https://github.com/CCBlueX/axochat_server/blob/master/PROTOCOL.md">Documentation</a>
 */
public interface AxochatPacket {
    interface Adaptor<I, O> {
        enum Type {
            CLIENT, SERVER;
        }

        /**
         * @return the type of adaptor implementation.
         */
        @NotNull Type type();

        /**
         * Read a packet instance from the input.
         */
        @NotNull AxochatPacket read(@NotNull I source) throws IOException;

        /**
         * Write a packet instance to the output.
         */
        void write(@NotNull O sink, @NotNull AxochatPacket packet) throws IOException;

    }
}
