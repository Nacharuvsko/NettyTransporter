package xyz.winston.nettytransporter.protocol.channel;

import io.netty.channel.socket.SocketChannel;
import lombok.Getter;
import lombok.NonNull;
import xyz.winston.nettytransporter.protocol.packet.ChannelProcessorContext;
import xyz.winston.nettytransporter.protocol.packet.handshake.processor.HandshakeServerProcessor;
import xyz.winston.nettytransporter.protocol.packet.play.processor.PlayServerProcessor;

public abstract class AbstractRemoteServerChannel extends AbstractRemoteChannel
        implements HandshakeServerProcessor, PlayServerProcessor {

    @Getter
    private final AbstractClientChannel client;

    public AbstractRemoteServerChannel(AbstractClientChannel client, SocketChannel channel) {
        super(channel);

        this.client = client;
    }

    @Override
    public void process(@NonNull ChannelProcessorContext ctx) throws Exception {
        ctx.callProcess(this);
        handlePacket(ctx);
    }

    @Override
    protected final void onDisconnect() {
        client.reconnect();
        onServerDisconnect();
    }

    protected void onServerDisconnect() {

    }
}
