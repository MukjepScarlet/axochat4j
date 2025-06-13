package moe.lasoleil.axochat4j.packet.c2s;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Value;
import moe.lasoleil.axochat4j.annotation.PacketMetadata;
import moe.lasoleil.axochat4j.packet.AxochatPacket;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * After the client received a MojangInfo packet and authenticating itself with mojang,
 * it has to send a LoginMojang packet to the server.
 * After the server receives a LoginMojang packet, it will send Success if the login was successful.
 */
@PacketMetadata(name = "LoginMojang")
@Value
public class C2SLoginMojangPacket implements AxochatPacket.C2S {

    /**
     * name needs to be associated with the uuid.
     */
    @SerializedName("name")
    @JsonProperty("name")
    @NotNull String name;

    /**
     * uuid is not guaranteed to be hyphenated.
     */
    @SerializedName("uuid")
    @JsonProperty("uuid")
    @NotNull UUID uuid;

    /**
     * allowMessages If allow_messages is true, other clients may send private messages to this client.
     */
    @SerializedName("allow_messages")
    @JsonProperty("allow_messages")
    boolean allowMessages;

}
