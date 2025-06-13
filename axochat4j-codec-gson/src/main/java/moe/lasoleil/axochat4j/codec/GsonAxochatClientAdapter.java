package moe.lasoleil.axochat4j.codec;

import com.google.gson.*;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import moe.lasoleil.axochat4j.annotation.PacketMetadata;
import moe.lasoleil.axochat4j.exception.MalformedPacketException;
import moe.lasoleil.axochat4j.exception.UnknownPacketNameException;
import moe.lasoleil.axochat4j.packet.AxochatPacket;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class GsonAxochatClientAdapter implements AxochatPacket.Adaptor<String, Appendable> {

    public static final GsonAxochatClientAdapter INSTANCE = new GsonAxochatClientAdapter();

    private GsonAxochatClientAdapter() {}

    private static final Gson DEFAULT_GSON = new Gson();

    /** Note: this constructor has been deprecated in the latest version. */
    private static final JsonParser JSON_PARSER = new JsonParser();

    private static final Map<String, Class<? extends AxochatPacket.S2C>> packetNameTypeMap;
    static {
        int size = AxochatPacket.S2C.ALL.size();
        Map<String, Class<? extends AxochatPacket.S2C>> _packetNameTypeMap = new HashMap<>(size, 1.0F);
        for (Class<? extends AxochatPacket.S2C> clazz : AxochatPacket.S2C.ALL){
            PacketMetadata metadata = AxochatPacket.metadata(clazz);
            _packetNameTypeMap.put(metadata.name(), clazz);
        }
        packetNameTypeMap = Collections.unmodifiableMap(_packetNameTypeMap);
    }

    @Override
    public @NotNull Type type() {
        return Type.CLIENT;
    }

    @NotNull
    @Override
    public AxochatPacket read(@NotNull String source) throws IOException {
        JsonElement src = JSON_PARSER.parse(source);

        if (!(src instanceof JsonObject))
            throw new MalformedPacketException("Source JSON " + src + " is not a JsonObject");

        JsonObject jsonObject = (JsonObject) src;
        JsonElement packetName = jsonObject.get("m");
        if (!(packetName instanceof JsonPrimitive))
            throw new MalformedPacketException("Packet name " + src + " is not a JsonPrimitive");

        String realPacketName = packetName.getAsString();
        Class<? extends AxochatPacket> type = packetNameTypeMap.get(realPacketName);
        if (type == null)
            throw new UnknownPacketNameException(realPacketName);

        PacketMetadata metadata = AxochatPacket.metadata(type);
        if (!metadata.noArg()) {
            JsonElement packetBody = jsonObject.get("c");
            if (!(packetBody instanceof JsonObject))
                throw new MalformedPacketException("Packet body " + packetBody + " is not a JsonObject");

            return DEFAULT_GSON.fromJson(packetBody, type);
        } else {
            return DEFAULT_GSON.fromJson("{}", type);
        }
    }

    @Override
    public void write(@NotNull Appendable sink, @NotNull AxochatPacket packet) throws IOException {
        JsonWriter writer = new JsonWriter(Streams.writerForAppendable(sink));
        writer.beginObject();
        PacketMetadata metadata = AxochatPacket.metadata(packet);
        writer.name("m").value(metadata.name());
        if (!metadata.noArg()) {
            writer.name("c");
            DEFAULT_GSON.toJson(packet, packet.getClass(), writer);
        }
        writer.endObject();
    }
}
