package moe.lasoleil.axochat4j.packet.c2s;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Value;
import moe.lasoleil.axochat4j.annotation.PacketMetadata;
import moe.lasoleil.axochat4j.packet.AxochatPacket;
import org.jetbrains.annotations.NotNull;

/**
 * The content of this packet will be sent to the specified client as PrivateMessage if it fits the validation scheme.
 */
@PacketMetadata(name = "PrivateMessage")
@Value
public class C2SPrivateMessagePacket implements AxochatPacket.C2S {

    /**
     * receiver is an ID.
     */
    @SerializedName("receiver")
    @JsonProperty("receiver")
    @NotNull String receiver;

    /**
     * content of the message.
     */
    @SerializedName("name")
    @JsonProperty("name")
    @NotNull String content;
}
