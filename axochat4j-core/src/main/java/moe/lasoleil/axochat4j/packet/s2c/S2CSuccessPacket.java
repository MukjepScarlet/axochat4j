package moe.lasoleil.axochat4j.packet.s2c;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Value;
import moe.lasoleil.axochat4j.annotation.PacketMetadata;
import moe.lasoleil.axochat4j.packet.AxochatPacket;
import org.jetbrains.annotations.NotNull;

/**
 * This packet is sent after either
 * {@link moe.lasoleil.axochat4j.packet.c2s.C2SLoginMojangPacket},
 * {@link moe.lasoleil.axochat4j.packet.c2s.C2SLoginJWTPacket},
 * {@link moe.lasoleil.axochat4j.packet.c2s.C2SBanUserPacket} or
 * {@link moe.lasoleil.axochat4j.packet.c2s.C2SUnbanUserPacket}
 * were processed successfully.
 */
@PacketMetadata(name = "Success")
@Value
public class S2CSuccessPacket implements AxochatPacket.S2C {

    /**
     * reason of success packet
     */
    @SerializedName("reason")
    @JsonProperty("reason")
    @NotNull String reason;

}
