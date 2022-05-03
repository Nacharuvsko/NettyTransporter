package xyz.winston.nettytransporter.connection;

import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.internal.SystemPropertyUtil;
import lombok.Getter;
import lombok.NonNull;
import xyz.winston.nettytransporter.ConnectorServer;
import xyz.winston.nettytransporter.protocol.channel.AbstractRemoteClientChannel;
import xyz.winston.nettytransporter.protocol.channel.AbstractServerChannel;
import xyz.winston.nettytransporter.protocol.packet.BossProcessor;
import xyz.winston.nettytransporter.protocol.packet.PacketProcessor;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author ItzStonlex (Скорее всего), Whilein, winston
 */
public class LocalServerConnection extends AbstractServerChannel {

    private final ConnectorServer core;

    public LocalServerConnection(
            final ConnectorServer core,
            final SocketAddress address,
            final int threads,
            final @NonNull Collection<PacketProcessor> processors
    ) {
        super(address, threads, processors);

        this.core = core;
    }

    @Override
    protected AbstractRemoteClientChannel newClientChannel(SocketChannel channel) {
        return new RemoteClientConnection(core, channel);
    }

    @Override
    protected void initPipeline(SocketChannel channel, Collection<PacketProcessor> processors) {
        super.initPipeline(channel, processors);

        try {
            channel.config().setOption(ChannelOption.IP_TOS, 0x18);
        } catch (ChannelException ignored) {
        }

        channel.config().setOption(ChannelOption.TCP_NODELAY, true);
        channel.config().setAllocator(PooledByteBufAllocator.DEFAULT);
    }
}
