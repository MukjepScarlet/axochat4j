package moe.lasoleil.axochat4j.exception;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class PacketIOException extends IOException {
    public PacketIOException(@NotNull Exception cause) {
        super(cause);
    }
}
