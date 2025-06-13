package moe.lasoleil.axochat4j.packet.s2c;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Value;
import moe.lasoleil.axochat4j.annotation.PacketMetadata;
import moe.lasoleil.axochat4j.packet.AxochatPacket;
import org.jetbrains.annotations.NotNull;

/**
 * After the client sent the server a RequestMojangInfo packet, the server will provide the client with a session_hash.
 * A session hash is synonymous with a server id in the context of authentication with Mojang.
 * The client has to send a LoginMojang packet to the server after authenticating itself with Mojang.
 */
@PacketMetadata(name = "MojangInfo")
@Value
public class S2CMojangInfoPacket implements AxochatPacket.S2C {

    /**
     * session_hash to authenticate with Mojang
     */
    @SerializedName("session_hash")
    @JsonProperty("session_hash")
    @NotNull String sessionHash;

}
