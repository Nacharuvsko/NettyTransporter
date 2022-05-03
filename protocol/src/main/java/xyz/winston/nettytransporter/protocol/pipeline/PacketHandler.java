package xyz.winston.nettytransporter.protocol.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import xyz.winston.nettytransporter.protocol.packet.BossProcessor;
import xyz.winston.nettytransporter.protocol.packet.ChannelProcessorContext;
import xyz.winston.nettytransporter.protocol.packet.Packet;

@RequiredArgsConstructor
public class PacketHandler extends SimpleChannelInboundHandler<Packet<?>> {

    @Getter
    private final BossProcessor processor;

    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx) throws Exception {
        processor.callActive();
    }

    @Override
    public void channelInactive(@NotNull ChannelHandlerContext ctx) throws Exception {
        processor.callInactive();
    }

    @Override
    public void channelRead0(@NotNull ChannelHandlerContext ctx, @NotNull Packet packet) {
        ChannelProcessorContext context = new ChannelProcessorContext(packet, processor, ctx);

        try {
            processor.callProcessor(context);
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            context.post();
        }
    }

    @Override
    public void exceptionCaught(@NotNull ChannelHandlerContext ctx, @NotNull Throwable cause) throws Exception {
        processor.callException(cause);
    }

}