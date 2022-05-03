package xyz.winston.nettytransporter.protocol.pipeline;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.NonNull;
import lombok.val;
import xyz.winston.nettytransporter.protocol.channel.AbstractChannel;
import xyz.winston.nettytransporter.protocol.packet.BossProcessor;
import xyz.winston.nettytransporter.protocol.packet.PacketProcessor;

import java.util.Collection;

/**
 * Простыми словами - конвейер пакетов
 * Каждый этап за что-то отвечает. Пример:
 * {@link #IN_LOG} - логирует входящие пакеты,
 * {@link #DECODER} - Используя маппер пакетов декодирует сообщение
 * @author winston
 */
public class Pipeline {

    public static final String FRAMER = "packet-framer";
    public static final String ENCODER = "packet-encoder";
    public static final String DECODER = "packet-decoder";
    public static final String HANDLER = "packet-handler";
    public static final String IN_LOG = "packet-inbound-logger";
    public static final String OUT_LOG = "packet-outbound-logger";

    public static void initPipeline(
            final @NonNull AbstractChannel channel,
            final @NonNull SocketChannel socket,
            final @NonNull Collection<PacketProcessor> processors
    ) {
        ChannelPipeline pipeline = socket.pipeline();

        val bossProcessor = channel.newPacketProcessor(socket);
        processors.forEach(bossProcessor::registerProcessor);

        pipeline.addLast(FRAMER, new PacketFramer());
        pipeline.addLast(ENCODER, new PacketEncoder(channel.getOutboundPacketDirection()));
        pipeline.addLast(DECODER, new PacketDecoder(channel.getInboundPacketDirection()));
        pipeline.addLast(IN_LOG, new PacketInboundLogger());
        pipeline.addLast(OUT_LOG, new PacketOutboundLogger());
        pipeline.addLast(HANDLER, new PacketHandler(bossProcessor));
    }

}
