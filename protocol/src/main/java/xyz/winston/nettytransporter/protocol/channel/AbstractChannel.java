package xyz.winston.nettytransporter.protocol.channel;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.socket.SocketChannel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import xyz.winston.nettytransporter.protocol.packet.BossProcessor;
import xyz.winston.nettytransporter.protocol.packet.PacketDirection;
import xyz.winston.nettytransporter.protocol.pipeline.Pipeline;

import java.net.SocketAddress;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
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

    protected void initPipeline(SocketChannel channel) {
        Pipeline.initPipeline(this, channel);
    }

    protected void initChannelInitializer() {
        channelInitializer = new ChannelInitializer<>() {
            @Override
            protected void initChannel(@NotNull SocketChannel ch) {
                initPipeline(ch);
            }
        };
    }

    protected void init() {
        initChannelInitializer();
    }

}
