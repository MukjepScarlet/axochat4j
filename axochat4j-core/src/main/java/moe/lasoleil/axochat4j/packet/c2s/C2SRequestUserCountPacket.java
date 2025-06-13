package moe.lasoleil.axochat4j.packet.c2s;

import lombok.Value;
import moe.lasoleil.axochat4j.annotation.PacketName;
import moe.lasoleil.axochat4j.packet.AxochatC2SPacket;

/**
 * After receiving this packet, the server will then send a UserCount packet to the client.
 *
 * This packet has no body.
 */
@PacketName("RequestUserCount")
@Value
public class C2SRequestUserCountPacket implements AxochatC2SPacket {
}
