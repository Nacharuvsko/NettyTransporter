package xyz.winston.nettytransporter.protocol.channel;

import io.netty.channel.socket.SocketChannel;
import lombok.NonNull;
import xyz.winston.nettytransporter.protocol.packet.ChannelProcessorContext;
import xyz.winston.nettytransporter.protocol.packet.handshake.processor.HandshakeClientProcessor;
import xyz.winston.nettytransporter.protocol.packet.play.processor.PlayClientProcessor;

public abstract class AbstractRemoteClientChannel extends AbstractRemoteChannel
        implements HandshakeClientProcessor, PlayClientProcessor {

    public AbstractRemoteClientChannel(SocketChannel channel) {
        super(channel);
    }

    @Override
    public void process(@NonNull ChannelProcessorContext ctx) throws Exception {
        ctx.callProcess(this);
        handlePacket(ctx);
    }
}
