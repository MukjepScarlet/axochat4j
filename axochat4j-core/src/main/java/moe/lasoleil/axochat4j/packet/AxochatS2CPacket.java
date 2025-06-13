package moe.lasoleil.axochat4j.packet;

import moe.lasoleil.axochat4j.packet.s2c.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <h2>AXOCHAT PROTOCOL</h2>
 * <a href="https://github.com/CCBlueX/axochat_server/blob/master/PROTOCOL.md">Documentation</a>
 * <p>
 *     <code>S2C</code> means packet from server to client.
 * </p>
 */
public interface AxochatS2CPacket extends AxochatPacket {

    List<Class<? extends AxochatS2CPacket>> ALL = Collections.unmodifiableList(
            Arrays.asList(
                    S2CErrorPacket.class,
                    S2CMessagePacket.class,
                    S2CMojangInfoPacket.class,
                    S2CNewJWTPacket.class,
                    S2CPrivateMessagePacket.class,
                    S2CSuccessPacket.class,
                    S2CUserCountPacket.class
            )
    );
}
