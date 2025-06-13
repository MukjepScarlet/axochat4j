package moe.lasoleil.axochat4j.packet.s2c;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Value;
import moe.lasoleil.axochat4j.annotation.PacketName;
import moe.lasoleil.axochat4j.packet.AxochatS2CPacket;
import org.jetbrains.annotations.NotNull;

/**
 * This packet may be sent at any time, but is usually a response to a failed action of the client.
 */
@PacketName("Error")
@Value
public class S2CErrorPacket implements AxochatS2CPacket {

    /**
     * error message.
     */
    @SerializedName("message")
    @JsonProperty("message")
    @NotNull String message;

}
