package moe.lasoleil.axochat4j.packet.c2s;

import lombok.Value;
import moe.lasoleil.axochat4j.annotation.PacketMetadata;
import moe.lasoleil.axochat4j.packet.AxochatPacket;

/**
 * To log in via mojang, the client has to send a RequestMojangInfo packet.
 * The server will then send a MojangInfo to the client.
 *
 * This packet has no body.
 */
@PacketMetadata(name = "RequestMojangInfo", noArg = true)
@Value
public class C2SRequestMojangInfoPacket implements AxochatPacket.C2S {
}
