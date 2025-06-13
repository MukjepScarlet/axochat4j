package moe.lasoleil.axochat4j.packet.s2c;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Value;
import moe.lasoleil.axochat4j.annotation.PacketMetadata;
import moe.lasoleil.axochat4j.packet.AxochatPacket;
import org.jetbrains.annotations.NotNull;

/**
 * This packet may be sent at any time, but is usually a response to a failed action of the client.
 */
@PacketMetadata(name = "Error")
@Value
public class S2CErrorPacket implements AxochatPacket.S2C {

    /**
     * error message.
     */
    @SerializedName("message")
    @JsonProperty("message")
    @NotNull String message;

}
