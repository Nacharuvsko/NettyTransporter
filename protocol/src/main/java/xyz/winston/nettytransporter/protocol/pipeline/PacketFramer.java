package xyz.winston.nettytransporter.protocol.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.CorruptedFrameException;
import lombok.val;
import xyz.winston.nettytransporter.protocol.packet.PacketUtils;

import java.util.List;

public class PacketFramer extends ByteToMessageCodec<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf from, ByteBuf to) {
        int packetSize = from.readableBytes();
        int headerSize = PacketUtils.getVarIntSize(packetSize);

        to.ensureWritable(packetSize + headerSize);

        PacketUtils.writeVarInt(to, packetSize);
        to.writeBytes(from, from.readerIndex(), packetSize);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) {
        buf.markReaderIndex();

        for (int i = 0; i < 3; ++i) {
            if (!buf.isReadable()) {
                buf.resetReaderIndex();
                return;
            }

            val b = buf.readByte();

            if (b >= 0) {
                buf.resetReaderIndex();

                int length = PacketUtils.readVarInt(buf);

                if (buf.readableBytes() < length) {
                    buf.resetReaderIndex();
                    return;
                }

                out.add(buf.slice(buf.readerIndex(), length).retain());
                buf.skipBytes(length);
                return;
            }
        }

        throw new CorruptedFrameException("length wider than 21-bit");
    }
}