package moe.lasoleil.axochat4j.packet.c2s;

import lombok.Value;
import moe.lasoleil.axochat4j.annotation.PacketMetadata;
import moe.lasoleil.axochat4j.packet.AxochatPacket;

/**
 * To log in using LoginJWT, a client needs to own a json web token.
 * This token can be retrieved by sending RequestJWT as an already authenticated client to the server.
 * The server will send a NewJWT packet to the client.
 *
 * This packet has no body.
 */
@PacketMetadata(name = "RequestJWT", noArg = true)
@Value
public class C2SRequestJWTPacket implements AxochatPacket.C2S {
}
