package xyz.winston.nettytransporter.protocol.packet.play.processor;

import xyz.winston.nettytransporter.protocol.packet.ChannelProcessorContext;
import xyz.winston.nettytransporter.protocol.packet.PacketProcessor;
import xyz.winston.nettytransporter.protocol.packet.play.*;

/**
 * @author winston
 */
public interface PlayProcessor extends PacketProcessor {

    default void process(B100TestPacket packet, ChannelProcessorContext ctx) {
        // override me
    }

    default void process(C01PacketClientExample packet, ChannelProcessorContext ctx) {
        // override me
    }

    default void process(S01PacketServerExample packet, ChannelProcessorContext ctx) {
        // override me
    }

}
