package xyz.winston.nettytransporter.protocol.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import xyz.winston.nettytransporter.protocol.packet.Packet;

@Log4j2
public final class PacketInboundLogger extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) throws Exception {
        if (msg instanceof Packet) {
            log.debug("Inbound packet: " + ctx.channel().remoteAddress() + " - " + msg);
        }

        super.channelRead(ctx, msg);
    }
}
