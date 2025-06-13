package moe.lasoleil.axochat4j.packet.s2c;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Value;
import moe.lasoleil.axochat4j.annotation.PacketName;
import moe.lasoleil.axochat4j.packet.AxochatC2SPacket;
import org.jetbrains.annotations.NotNull;

/**
 * This packet is sent after either
 * {@link moe.lasoleil.axochat4j.packet.c2s.C2SLoginMojangPacket},
 * {@link moe.lasoleil.axochat4j.packet.c2s.C2SLoginJWTPacket},
 * {@link moe.lasoleil.axochat4j.packet.c2s.C2SBanUserPacket} or
 * {@link moe.lasoleil.axochat4j.packet.c2s.C2SUnbanUserPacket}
 * were processed successfully.
 */
@PacketName("Success")
@Value
public class S2CSuccessPacket implements AxochatC2SPacket {

    /**
     * reason of success packet
     */
    @SerializedName("reason")
    @JsonProperty("reason")
    @NotNull String reason;

}
