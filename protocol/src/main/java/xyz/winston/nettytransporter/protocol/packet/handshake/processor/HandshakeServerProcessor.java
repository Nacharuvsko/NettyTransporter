package xyz.winston.nettytransporter.protocol.packet.handshake.processor;

import xyz.winston.nettytransporter.protocol.packet.ChannelProcessorContext;
import xyz.winston.nettytransporter.protocol.packet.handshake.Handshake;

/**
 * Обрабатывает пакеты ОТ сервера
 * @author winston
 */
public interface HandshakeServerProcessor extends HandshakeProcessor {

    default void process(Handshake.Response packet, ChannelProcessorContext ctx) {
        // override me
    }

}
