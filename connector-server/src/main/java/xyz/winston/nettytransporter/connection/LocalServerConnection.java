package xyz.winston.nettytransporter.connection;

import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.internal.SystemPropertyUtil;
import xyz.winston.nettytransporter.ConnectorServer;
import xyz.winston.nettytransporter.protocol.channel.AbstractRemoteClientChannel;
import xyz.winston.nettytransporter.protocol.channel.AbstractServerChannel;

import java.net.SocketAddress;

public class LocalServerConnection extends AbstractServerChannel {

    private final ConnectorServer core;

    public LocalServerConnection(ConnectorServer core, SocketAddress address, int threads) {
        super(address, threads);

        this.core = core;
    }

    @Override
    protected AbstractRemoteClientChannel newClientChannel(SocketChannel channel) {
        return new RemoteClientConnection(core, channel);
    }

    @Override
    protected void initPipeline(SocketChannel channel) {
        super.initPipeline(channel);

        try {
            channel.config().setOption(ChannelOption.IP_TOS, 0x18);
        } catch (ChannelException ignored) {
        }

        channel.config().setOption(ChannelOption.TCP_NODELAY, true);
        channel.config().setAllocator(PooledByteBufAllocator.DEFAULT);
    }
}
