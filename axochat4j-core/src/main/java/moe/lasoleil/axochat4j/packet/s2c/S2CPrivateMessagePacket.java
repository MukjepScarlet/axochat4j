package moe.lasoleil.axochat4j.packet.s2c;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Value;
import moe.lasoleil.axochat4j.annotation.PacketName;
import moe.lasoleil.axochat4j.model.AxochatUser;
import moe.lasoleil.axochat4j.packet.AxochatS2CPacket;
import org.jetbrains.annotations.NotNull;

/**
 * This packet will be sent to an authenticated client with allow_messages turned on,
 * if another client successfully sent a private message to the server with the id.
 */
@PacketName("PrivateMessage")
@Value
public class S2CPrivateMessagePacket implements AxochatS2CPacket {

    /**
     * author_info is optional and described in detail in UserInfo.
     */
    @SerializedName("author_info")
    @JsonProperty("author_info")
    @NotNull AxochatUser authorInfo;

    /**
     * content is any message fitting the validation scheme of the server
     */
    @SerializedName("content")
    @JsonProperty("content")
    @NotNull String content;

}
