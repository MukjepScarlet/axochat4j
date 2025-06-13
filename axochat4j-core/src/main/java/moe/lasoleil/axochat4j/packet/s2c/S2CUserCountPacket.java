package moe.lasoleil.axochat4j.packet.s2c;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Value;
import moe.lasoleil.axochat4j.annotation.PacketName;
import moe.lasoleil.axochat4j.packet.AxochatC2SPacket;

/**
 * This packet is sent after {@link moe.lasoleil.axochat4j.packet.c2s.C2SRequestUserCountPacket} was received.
 */
@PacketName("UserCount")
@Value
public class S2CUserCountPacket implements AxochatC2SPacket {

    /**
     * connections is the number of connections this server has open
     */
    @SerializedName("connections")
    @JsonProperty("connections")
    int connections;

    /**
     * logged_in is the amount of authenticated connections this server has open
     */
    @SerializedName("logged_in")
    @JsonProperty("logged_in")
    int loggedIn;

}
