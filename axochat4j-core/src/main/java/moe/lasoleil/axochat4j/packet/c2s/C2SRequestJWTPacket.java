package moe.lasoleil.axochat4j.packet.c2s;

import lombok.Value;
import moe.lasoleil.axochat4j.annotation.PacketName;
import moe.lasoleil.axochat4j.packet.AxochatC2SPacket;

/**
 * To log in using LoginJWT, a client needs to own a json web token.
 * This token can be retrieved by sending RequestJWT as an already authenticated client to the server.
 * The server will send a NewJWT packet to the client.
 *
 * This packet has no body.
 */
@PacketName("RequestJWT")
@Value
public class C2SRequestJWTPacket implements AxochatC2SPacket {
}
