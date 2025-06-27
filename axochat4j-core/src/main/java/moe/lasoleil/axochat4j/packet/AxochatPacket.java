package moe.lasoleil.axochat4j.packet;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Value;
import moe.lasoleil.axochat4j.annotation.PacketMetadata;
import moe.lasoleil.axochat4j.exception.MalformedPacketException;
import moe.lasoleil.axochat4j.exception.PacketIOException;
import moe.lasoleil.axochat4j.packet.c2s.*;
import moe.lasoleil.axochat4j.packet.s2c.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <h2>AXOCHAT PROTOCOL</h2>
 * <a href="https://github.com/CCBlueX/axochat_server/blob/master/PROTOCOL.md">Documentation</a>
 *
 * <p>All implementations should have {@link PacketMetadata} annotation.</p>
 */
public interface AxochatPacket {

    static PacketMetadata metadata(@NotNull AxochatPacket packet) {
        return metadata(Objects.requireNonNull(packet.getClass()));
    }

    static PacketMetadata metadata(@NotNull Class<? extends AxochatPacket> packetClass) {
        return Objects.requireNonNull(packetClass.getAnnotation(PacketMetadata.class));
    }

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
            PacketMetadata metadata = metadata(packet);
            return new Full(metadata.name(), metadata.noArg() ? null : packet);
        }
    }

    /**
     * The adaptor is for input and output.
     */
    interface Adaptor {
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
        @NotNull AxochatPacket read(@NotNull String source) throws PacketIOException, MalformedPacketException;

        /**
         * Write a packet instance to the output.
         */
        void write(@NotNull Appendable sink, @NotNull AxochatPacket packet) throws PacketIOException;

    }

    /**
     * <h2>AXOCHAT PROTOCOL</h2>
     * <a href="https://github.com/CCBlueX/axochat_server/blob/master/PROTOCOL.md">Documentation</a>
     * <p>
     *     <code>C2S</code> means packet from client to server.
     * </p>
     */
    interface C2S extends AxochatPacket {

        List<Class<? extends C2S>> ALL = Collections.unmodifiableList(
                Arrays.asList(
                        C2SBanUserPacket.class,
                        C2SLoginJWTPacket.class,
                        C2SLoginMojangPacket.class,
                        C2SMessagePacket.class,
                        C2SPrivateMessagePacket.class,
                        C2SRequestJWTPacket.class,
                        C2SRequestMojangInfoPacket.class,
                        C2SRequestUserCountPacket.class,
                        C2SUnbanUserPacket.class
                )
        );

    }

    /**
     * <h2>AXOCHAT PROTOCOL</h2>
     * <a href="https://github.com/CCBlueX/axochat_server/blob/master/PROTOCOL.md">Documentation</a>
     * <p>
     *     <code>S2C</code> means packet from server to client.
     * </p>
     */
    interface S2C extends AxochatPacket {

        List<Class<? extends S2C>> ALL = Collections.unmodifiableList(
                Arrays.asList(
                        S2CErrorPacket.class,
                        S2CMessagePacket.class,
                        S2CMojangInfoPacket.class,
                        S2CNewJWTPacket.class,
                        S2CPrivateMessagePacket.class,
                        S2CSuccessPacket.class,
                        S2CUserCountPacket.class
                )
        );
    }
}
