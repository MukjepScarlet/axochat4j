package moe.lasoleil.axochat4j.packet;

import moe.lasoleil.axochat4j.packet.c2s.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <h2>AXOCHAT PROTOCOL</h2>
 * <a href="https://github.com/CCBlueX/axochat_server/blob/master/PROTOCOL.md">Documentation</a>
 * <p>
 *     <code>C2S</code> means packet from client to server.
 * </p>
 */
public interface AxochatC2SPacket extends AxochatPacket {

    List<Class<? extends AxochatC2SPacket>> ALL = Collections.unmodifiableList(
            Arrays.asList(
                    C2SBanUserPacket.class,
                    C2SLoginJWTPacket.class,
                    C2SLoginMojangPacket.class,
                    C2SMessagePacket.class,
                    C2SPrivateMessagePacket.class,
                    C2SRequestJWTPacket.class,
                    C2SRequestMojangInfoPacket.class,
                    C2SRequestUserCountPacket.class,
                    C2SUnbanUserPacket.class
            )
    );

}
