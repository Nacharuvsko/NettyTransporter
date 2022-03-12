package xyz.winston.nettytransporter.protocol.packet;

import lombok.NonNull;

/**
 * @author winston
 */
public interface PacketProcessor {

    /**
     * Process error
     */
    default void process(@NonNull Throwable throwable) throws Exception {
        throwable.printStackTrace();

        // for implementation
    }

    /**
     * Process packet
     */
    default void process(ChannelProcessorContext ctx) throws Exception {
        ctx.callProcess(this);
    }

    /**
     * Process connect
     */
    default void active() throws Exception {
        // for implementation
    }

    /**
     * Process disconnect
     */
    default void inactive() throws Exception {
        // for implementation
    }


}
