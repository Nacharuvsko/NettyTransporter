package xyz.winston.nettytransporter.protocol.packet.handshake.processor;

import xyz.winston.nettytransporter.protocol.packet.ChannelProcessorContext;
import xyz.winston.nettytransporter.protocol.packet.handshake.Handshake;

/**
 * Обрабатывает пакеты ОТ клиента
 * @author winston
 */
public interface HandshakeClientProcessor extends HandshakeProcessor {

    default void process(Handshake.Request packet, ChannelProcessorContext ctx) {
        // override me
    }

}
