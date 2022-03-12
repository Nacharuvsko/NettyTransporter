package xyz.winston.nettytransporter.protocol.pipeline;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import xyz.winston.nettytransporter.protocol.channel.AbstractChannel;

public class Pipeline {

    public static final String FRAMER = "packet-framer";
    public static final String ENCODER = "packet-encoder";
    public static final String DECODER = "packet-decoder";
    public static final String HANDLER = "packet-handler";
    public static final String IN_LOG = "packet-inbound-logger";
    public static final String OUT_LOG = "packet-outbound-logger";

    public static void initPipeline(AbstractChannel channel, SocketChannel socket) {
        ChannelPipeline pipeline = socket.pipeline();

        pipeline.addLast(FRAMER, new PacketFramer());
        pipeline.addLast(ENCODER, new PacketEncoder(channel.getOutboundPacketDirection()));
        pipeline.addLast(DECODER, new PacketDecoder(channel.getInboundPacketDirection()));
        pipeline.addLast(IN_LOG, new PacketInboundLogger());
        pipeline.addLast(OUT_LOG, new PacketOutboundLogger());
        pipeline.addLast(HANDLER, new PacketHandler(channel.newPacketProcessor(socket)));
    }

}
