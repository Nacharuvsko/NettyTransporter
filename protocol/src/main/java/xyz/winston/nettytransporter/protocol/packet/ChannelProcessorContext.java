package xyz.winston.nettytransporter.protocol.packet;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import lombok.NonNull;
import lombok.Setter;
import lombok.val;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class ChannelProcessorContext {

    private final Packet packet;

    // первый процессор, который указан в PacketHandler
    private final BossProcessor mainProcessor;

    private final ChannelHandlerContext context;

    private final AtomicInteger latch = new AtomicInteger();
    private final AtomicBoolean fired = new AtomicBoolean();

    @Setter
    private boolean shouldClose;

    private Packet<?> response;

    public ChannelProcessorContext(Packet<?> packet, BossProcessor mainProcessor, ChannelHandlerContext context) {
        this.packet = packet;
        this.mainProcessor = mainProcessor;
        this.context = context;
    }

    public void addLatch() {
        latch.incrementAndGet();
    }

    public void removeLatch() {
        if (latch.decrementAndGet() == 0 && fired.get()) {
            done();
        }
    }

    @SuppressWarnings("unchecked")
    public void callProcess(PacketProcessor processor) throws Exception {
        packet.process(processor, this);
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public BossProcessor getMainProcessor() {
        return mainProcessor;
    }

    public ChannelHandlerContext getContext() {
        return context;
    }

    public Packet<?> getResponse() {
        return response;
    }

    /**
     * Установить ответ на запрос
     * <p>
     * Если пришёл не запрос, ответ всё равно отправится,
     * чтобы этого не было, используйте {@link #setStrictResponse}
     */
    public void setResponse(@NonNull Packet.Response<?> response) {
        this.response = response;

        if (!(packet instanceof Packet.Request)) return;

        val request = (Packet.Request<?, ?>) packet;
        if (!request.hasRequestId()) return;

        response.setRequestId(request.getRequestId());
    }

    /**
     * Установить ответ на запрос
     * <p>
     * Если пришёл не запрос, ответ не отправится,
     * чтобы этого не было, используйте {@link #setResponse}
     */
    public void setStrictResponse(@NonNull Supplier<Packet.Response<?>> supplier) {
        if (!(packet instanceof Packet.Request)) return;

        val request = (Packet.Request<?, ?>) packet;
        if (!request.hasRequestId()) return;

        setResponse(supplier.get());
    }

    public void done() {
        if (response != null) {
            if (shouldClose) {
                context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                context.writeAndFlush(response, context.voidPromise());
            }
        }
    }

    /** invoked in {@link xyz.winston.nettytransporter.protocol.pipeline.PacketHandler#channelRead0(ChannelHandlerContext, Packet) PacketHandler.channelRead0()} */
    public void post() {
        if (latch.get() == 0) {
            done();
        }

        fired.set(true);
    }


}
