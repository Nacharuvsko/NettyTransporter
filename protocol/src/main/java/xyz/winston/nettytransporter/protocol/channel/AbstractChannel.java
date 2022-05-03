package xyz.winston.nettytransporter.protocol.channel;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.socket.SocketChannel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import xyz.winston.nettytransporter.protocol.packet.BossProcessor;
import xyz.winston.nettytransporter.protocol.packet.PacketDirection;
import xyz.winston.nettytransporter.protocol.packet.PacketProcessor;
import xyz.winston.nettytransporter.protocol.pipeline.Pipeline;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Log4j2
@RequiredArgsConstructor
public abstract class AbstractChannel {

    protected static final boolean epoll = Epoll.isAvailable();

    protected static final ThreadFactory threadFactory = new ThreadFactory() {

        private final AtomicInteger counter = new AtomicInteger();

        @Override
        public Thread newThread(@NotNull Runnable r) {
            return new Thread(r, "[Netty] EventLoopGroup #" + counter.incrementAndGet());
        }
    };

    protected ChannelInitializer<SocketChannel> channelInitializer;

    protected final SocketAddress socketAddress;
    protected final int threads;

    public SocketAddress getSocketAddress() {
        return socketAddress;
    }

    public abstract BossProcessor newPacketProcessor(SocketChannel channel);

    public abstract PacketDirection getOutboundPacketDirection();

    public abstract PacketDirection getInboundPacketDirection();

    public boolean isEpoll() {
        return epoll;
    }

    protected void initPipeline(SocketChannel channel, Collection<PacketProcessor> customProcessors) {
        Pipeline.initPipeline(this, channel, customProcessors);
    }

    protected void initChannelInitializer(Collection<PacketProcessor> customProcessors) {
        channelInitializer = new ChannelInitializer<>() {
            @Override
            protected void initChannel(@NotNull SocketChannel ch) {
                initPipeline(ch, customProcessors);
            }
        };
    }

    protected void init(Collection<PacketProcessor> customProcessors) {
        initChannelInitializer(customProcessors);
    }

}
