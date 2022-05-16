package xyz.winston.nettytransporter.protocol.channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;
import xyz.winston.nettytransporter.protocol.conf.ClientConfiguration;
import xyz.winston.nettytransporter.protocol.exception.ConnectException;
import xyz.winston.nettytransporter.protocol.packet.BossProcessor;
import xyz.winston.nettytransporter.protocol.packet.Packet;
import xyz.winston.nettytransporter.protocol.packet.PacketDirection;
import xyz.winston.nettytransporter.protocol.packet.PacketProcessor;

import java.net.SocketAddress;
import java.nio.channels.AlreadyConnectedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Getter
@Log4j2
public abstract class AbstractClientChannel extends AbstractChannel {

    protected EventLoopGroup worker;

    protected Class<? extends SocketChannel> channelClass;

    protected Bootstrap bootstrap;

    private ChannelFuture future;

    private AbstractRemoteServerChannel channel;

    private ClientConfiguration clientConfiguration;

    public AbstractClientChannel(
            final @NonNull SocketAddress address,
            final @NonNull ClientConfiguration clientConfiguration,
            final int threads
    ) {
        super(address, threads);
        this.clientConfiguration = clientConfiguration;

        init(new ArrayList<>()); // пустой, ибо мы регаем процессоры в Connection.registerProcessor()
    }

    protected void ensureConnectionAvailability() throws ConnectException {
        if (isConnected()) {
            throw new ConnectException(this, new AlreadyConnectedException());
        }
    }

    public void connectAsynchronous() {
        connectAsynchronous(ConnectException::printStackTrace, null);
    }

    public void connectAsynchronous(@NonNull Consumer<ConnectException> errorHandler) {
        connectAsynchronous(errorHandler, null);
    }

    protected boolean reconnecting;

    public void reconnect() {
        if (!clientConfiguration.isAutoReconnect()) return;
        if (reconnecting) {
            return;
        }

        reconnecting = true;
        scheduleReconnect();
    }

    protected void scheduleReconnect() {
        log.warn("Reconnecting to server in 5 seconds...");
        worker.schedule(this::doReconnect, 5, TimeUnit.SECONDS);
    }

    protected void doReconnect() {
        try {
            connectSynchronized();

            reconnecting = false;
        } catch (Exception e) {
            log.error("Unable to reconnect: " + e);

            scheduleReconnect();
        }
    }

    public void connectAsynchronous(@Nullable Runnable success) {
        connectAsynchronous(ConnectException::printStackTrace, success);
    }

    @Override
    public PacketDirection getOutboundPacketDirection() {
        return PacketDirection.TO_SERVER;
    }

    @Override
    public PacketDirection getInboundPacketDirection() {
        return PacketDirection.TO_CLIENT;
    }


    protected abstract AbstractRemoteServerChannel newServerChannel(SocketChannel channel);

    public void connectAsynchronous(@NonNull Consumer<ConnectException> errorHandler, @Nullable Runnable success) {
        try {
            ensureConnectionAvailability();

            future = bootstrap.connect();

            future.addListener(future -> {
                if (future.isSuccess()) {
                    if (success != null) {
                        success.run();
                    }
                } else {
                    errorHandler.accept(new ConnectException(this, future.cause()));
                }

                this.future = null;
            });
        } catch (Exception e) {
            errorHandler.accept(new ConnectException(this, e));
        }
    }

    public void connectSynchronized() throws ConnectException {
        ensureConnectionAvailability();

        try {
            future = bootstrap.connect().sync();

            if (!future.isSuccess()) {
                throw future.cause();
            }
        } catch (Throwable t) {
            throw new ConnectException(this, t);
        } finally {
            future = null;
        }
    }

    protected void closeChannel() {
        channel.close();
    }

    protected void interruptFuture() {
        future.cancel(true);
        future = null;
    }

    public void closeConnection() {
        if (isConnected()) {
            closeChannel();
        } else if (future != null) {
            interruptFuture();
        }
    }

    public boolean isConnected() {
        return channel != null && channel.isActive();
    }

    public boolean isConnecting() {
        return future != null;
    }

    @Override
    protected void init(Collection<PacketProcessor> customProcessors) {
        super.init(customProcessors);

        initChannelClass();
        initEventLoopGroups();
        initBootstrap();
    }

    @Override
    public BossProcessor newPacketProcessor(SocketChannel channel) {
        return this.channel = newServerChannel(channel);
    }

    protected void initChannelClass() {
        channelClass = epoll ? EpollSocketChannel.class : NioSocketChannel.class;
    }

    protected void initEventLoopGroups() {
        worker = epoll
                ? new EpollEventLoopGroup(threads, threadFactory)
                : new NioEventLoopGroup(threads, threadFactory);
    }

    protected void initBootstrap() {
        bootstrap = new Bootstrap()
                .option(ChannelOption.TCP_NODELAY, true)
                .remoteAddress(socketAddress)
                .channel(channelClass)
                .handler(channelInitializer)
                .group(worker);
    }

    public void sendPacket(Packet<?> packet) {
        if (isConnected()) {
            channel.sendPacket(packet);
        }
    }

    public <A extends Packet.Response<?>, B extends Packet.Request<?, A>> CompletableFuture<A> awaitPacket(@NonNull B packet) {
        return channel.awaitPacket(packet);
    }

    public <A extends Packet.Response<?>, B extends Packet.Request<?, A>> CompletableFuture<A> awaitPacket(@NonNull B packet, long timeout) {
        return channel.awaitPacket(packet, timeout);
    }
}
