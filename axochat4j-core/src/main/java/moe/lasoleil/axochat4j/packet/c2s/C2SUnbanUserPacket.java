package moe.lasoleil.axochat4j.packet.c2s;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Value;
import moe.lasoleil.axochat4j.annotation.PacketName;
import moe.lasoleil.axochat4j.packet.AxochatC2SPacket;
import org.jetbrains.annotations.NotNull;

/**
 * A client can send this packet to unban other users.
 */
@PacketName("UnbanUser")
@Value
public class C2SUnbanUserPacket implements AxochatC2SPacket {

    /**
     * user is an ID.
     */
    @SerializedName("user")
    @JsonProperty("user")
    @NotNull String user;

}
