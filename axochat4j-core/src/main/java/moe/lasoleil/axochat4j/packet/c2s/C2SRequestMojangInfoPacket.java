package moe.lasoleil.axochat4j.packet.c2s;

import lombok.Value;
import moe.lasoleil.axochat4j.annotation.PacketName;
import moe.lasoleil.axochat4j.packet.AxochatC2SPacket;

/**
 * To log in via mojang, the client has to send a RequestMojangInfo packet.
 * The server will then send a MojangInfo to the client.
 *
 * This packet has no body.
 */
@PacketName("RequestMojangInfo")
@Value
public class C2SRequestMojangInfoPacket implements AxochatC2SPacket {
}
