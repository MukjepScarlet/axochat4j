package moe.lasoleil.axochat4j.codec;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import moe.lasoleil.axochat4j.annotation.PacketMetadata;
import moe.lasoleil.axochat4j.exception.MalformedPacketException;
import moe.lasoleil.axochat4j.exception.PacketIOException;
import moe.lasoleil.axochat4j.exception.UnknownPacketNameException;
import moe.lasoleil.axochat4j.packet.AxochatPacket;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class JacksonAxochatClientAdapter implements AxochatPacket.Adaptor<String, OutputStream> {

    public static final JacksonAxochatClientAdapter INSTANCE = new JacksonAxochatClientAdapter();

    private JacksonAxochatClientAdapter() {}

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final ObjectNode EMPTY = OBJECT_MAPPER.createObjectNode();

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
    public AxochatPacket read(@NotNull String source) throws PacketIOException, MalformedPacketException {
        try {
            JsonNode src = OBJECT_MAPPER.readTree(source);

            if (!(src instanceof ObjectNode))
                throw new MalformedPacketException("Source JSON " + src + " is not an ObjectNode");

            ObjectNode jsonObject = (ObjectNode) src;
            JsonNode packetNameNode = jsonObject.get("m");
            if (packetNameNode == null || !packetNameNode.isTextual())
                throw new MalformedPacketException("Packet name " + packetNameNode + " is not a string");

            String realPacketName = packetNameNode.asText();
            Class<? extends AxochatPacket> type = packetNameTypeMap.get(realPacketName);
            if (type == null)
                throw new UnknownPacketNameException(realPacketName);

            PacketMetadata metadata = AxochatPacket.metadata(type);
            if (!metadata.noArg()) {
                JsonNode packetBody = jsonObject.get("c");
                if (packetBody == null || !packetBody.isObject())
                    throw new MalformedPacketException("Packet body " + packetBody + " is not an ObjectNode");

                return OBJECT_MAPPER.treeToValue(packetBody, type);
            } else {
                return OBJECT_MAPPER.treeToValue(EMPTY, type);
            }
        } catch (IOException e) {
            throw new PacketIOException(e);
        }
    }

    @Override
    public void write(@NotNull OutputStream sink, @NotNull AxochatPacket packet) throws PacketIOException {
        try {
            PacketMetadata metadata = AxochatPacket.metadata(packet);
            JsonGenerator generator = OBJECT_MAPPER.getFactory().createGenerator(new OutputStreamWriter(sink, StandardCharsets.UTF_8));
            generator.writeStartObject();
            generator.writeStringField("m", metadata.name());
            if (!metadata.noArg()) {
                generator.writeFieldName("c");
                OBJECT_MAPPER.writeValue(generator, packet);
            }
            generator.writeEndObject();
            generator.flush();
        } catch (IOException e) {
            throw new PacketIOException(e);
        }
    }
}
