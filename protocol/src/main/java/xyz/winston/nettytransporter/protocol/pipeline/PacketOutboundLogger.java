package xyz.winston.nettytransporter.protocol.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import lombok.extern.log4j.Log4j2;
import xyz.winston.nettytransporter.protocol.packet.Packet;

@Log4j2
public final class PacketOutboundLogger extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof Packet) {
            log.debug("Outbound packet: " + ctx.channel().remoteAddress() + " - " + msg);
        }

        super.write(ctx, msg, promise);
    }
}
