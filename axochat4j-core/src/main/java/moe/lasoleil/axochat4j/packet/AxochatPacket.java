package moe.lasoleil.axochat4j.packet;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Value;
import moe.lasoleil.axochat4j.annotation.PacketName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

/**
 * <h2>AXOCHAT PROTOCOL</h2>
 * <a href="https://github.com/CCBlueX/axochat_server/blob/master/PROTOCOL.md">Documentation</a>
 *
 * <p>All implementations should have {@link PacketName} annotation.</p>
 */
public interface AxochatPacket {

    /**
     * The full packet is for serialization.
     */
    @Value
    class Full {
        @SerializedName("m")
        @JsonProperty("m")
        @NotNull String packetName;

        @SerializedName("c")
        @JsonProperty("c")
        @Nullable AxochatPacket packetContent;

        /**
         * Make a full packet from a packet body, auto read packet name from annotation.
         */
        public static Full of(@NotNull AxochatPacket packet) {
            PacketName packetName = Objects.requireNonNull(packet.getClass().getAnnotation(PacketName.class));
            return new Full(packetName.value(), Objects.requireNonNull(packet));
        }
    }

    /**
     * The adaptor is for input and output.
     */
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
