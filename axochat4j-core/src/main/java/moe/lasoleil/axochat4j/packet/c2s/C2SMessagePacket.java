package moe.lasoleil.axochat4j.packet.c2s;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Value;
import moe.lasoleil.axochat4j.annotation.PacketMetadata;
import moe.lasoleil.axochat4j.packet.AxochatPacket;
import org.jetbrains.annotations.NotNull;

/**
 * The content of this packet will be sent to every client as Message if it fits the validation scheme.
 */
@PacketMetadata(name = "Message")
@Value
public class C2SMessagePacket implements AxochatPacket.C2S {

    /**
     * content of the message.
     */
    @SerializedName("content")
    @JsonProperty("content")
    @NotNull String content;
}
