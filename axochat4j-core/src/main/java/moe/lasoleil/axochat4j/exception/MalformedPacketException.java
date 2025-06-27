package moe.lasoleil.axochat4j.exception;

import org.jetbrains.annotations.NotNull;

public class MalformedPacketException extends Exception {
    public MalformedPacketException(@NotNull String message) {
        super(message);
    }
}
