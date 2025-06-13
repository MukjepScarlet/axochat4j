package moe.lasoleil.axochat4j.packet.c2s;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Value;
import moe.lasoleil.axochat4j.annotation.PacketMetadata;
import moe.lasoleil.axochat4j.packet.AxochatPacket;
import org.jetbrains.annotations.NotNull;

/**
 * To log in using a JWT, the client has to send a LoginJWT packet.
 * It will send Success if the login was successful.
 */
@PacketMetadata(name = "LoginJWT")
@Value
public class C2SLoginJWTPacket implements AxochatPacket.C2S {

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
