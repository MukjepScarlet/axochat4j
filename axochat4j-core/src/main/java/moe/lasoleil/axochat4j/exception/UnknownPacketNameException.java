package moe.lasoleil.axochat4j.exception;

import org.jetbrains.annotations.NotNull;

public final class UnknownPacketNameException extends MalformedPacketException {
    public UnknownPacketNameException(@NotNull String packetName) {
        super("Unknown packet name: " + packetName);
    }
}
