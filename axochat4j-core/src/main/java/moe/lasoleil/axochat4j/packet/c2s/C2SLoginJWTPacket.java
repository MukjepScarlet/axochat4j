package moe.lasoleil.axochat4j.packet.c2s;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Value;
import moe.lasoleil.axochat4j.annotation.PacketName;
import moe.lasoleil.axochat4j.packet.AxochatC2SPacket;
import org.jetbrains.annotations.NotNull;

/**
 * To log in using a JWT, the client has to send a LoginJWT packet.
 * It will send Success if the login was successful.
 */
@PacketName("LoginJWT")
@Value
public class C2SLoginJWTPacket implements AxochatC2SPacket {

    /**
     * token can be retrieved by sending RequestJWT on an already authenticated connection.
     */
    @SerializedName("token")
    @JsonProperty("token")
    @NotNull String token;

    /**
     * allowMessages If allow_messages is true, other clients may send private messages to this client.
     */
    @SerializedName("allow_messages")
    @JsonProperty("allow_messages")
    boolean allowMessages;

}
