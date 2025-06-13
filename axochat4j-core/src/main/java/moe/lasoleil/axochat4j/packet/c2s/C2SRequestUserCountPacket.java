package moe.lasoleil.axochat4j.packet.c2s;

import lombok.Value;
import moe.lasoleil.axochat4j.annotation.PacketMetadata;
import moe.lasoleil.axochat4j.packet.AxochatPacket;

/**
 * After receiving this packet, the server will then send a UserCount packet to the client.
 *
 * This packet has no body.
 */
@PacketMetadata(name = "RequestUserCount", noArg = true)
@Value
public class C2SRequestUserCountPacket implements AxochatPacket.C2S {
}
