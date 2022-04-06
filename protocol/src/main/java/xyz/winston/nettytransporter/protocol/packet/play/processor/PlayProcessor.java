package xyz.winston.nettytransporter.protocol.packet.play.processor;

import org.jetbrains.annotations.NotNull;
import xyz.winston.nettytransporter.protocol.packet.ChannelProcessorContext;
import xyz.winston.nettytransporter.protocol.packet.PacketProcessor;
import xyz.winston.nettytransporter.protocol.packet.play.*;

/**
 * @author winston
 */
public interface PlayProcessor extends PacketProcessor {

    default void process(final @NotNull B100TestPacket packet, final @NotNull ChannelProcessorContext ctx) {
        // override me
    }

    default void process(final @NotNull C01PacketClientExample packet, final @NotNull ChannelProcessorContext ctx) {
        // override me
    }

    default void process(final @NotNull S01PacketServerExample packet, final @NotNull ChannelProcessorContext ctx) {
        // override me
    }

}
