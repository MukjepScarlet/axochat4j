package moe.lasoleil.axochat4j.exception;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class UnknownPacketNameException extends IOException {
    public UnknownPacketNameException(@NotNull String packetName) {
        super("Unknown packet name: " + packetName);
    }
}
