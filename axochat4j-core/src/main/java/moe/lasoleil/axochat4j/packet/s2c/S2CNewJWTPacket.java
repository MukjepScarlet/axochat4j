package moe.lasoleil.axochat4j.packet.s2c;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Value;
import moe.lasoleil.axochat4j.annotation.PacketName;
import moe.lasoleil.axochat4j.packet.AxochatC2SPacket;
import org.jetbrains.annotations.NotNull;

/**
 * After the client sent the server a {@link moe.lasoleil.axochat4j.packet.c2s.C2SRequestJWTPacket}
 * packet, the server will provide the client with JWT.
 * This token can be used in the {@link moe.lasoleil.axochat4j.packet.c2s.C2SLoginJWTPacket} packet.
 */
@PacketName("NewJWT")
@Value
public class S2CNewJWTPacket implements AxochatC2SPacket {

    /**
     * JWT token
     */
    @SerializedName("token")
    @JsonProperty("token")
    @NotNull String token;

}
