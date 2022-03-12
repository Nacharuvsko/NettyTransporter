package xyz.winston.nettytransporter.protocol.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;
import xyz.winston.nettytransporter.protocol.packet.Packet;
import xyz.winston.nettytransporter.protocol.packet.PacketDirection;
import xyz.winston.nettytransporter.protocol.packet.PacketProtocol;
import xyz.winston.nettytransporter.protocol.packet.PacketUtils;
import xyz.winston.nettytransporter.protocol.pipeline.exception.BadPacketException;
import xyz.winston.nettytransporter.protocol.pipeline.exception.DecoderException;

import java.util.List;

@RequiredArgsConstructor
public class PacketDecoder extends ByteToMessageDecoder {

    private final PacketDirection direction;
    private PacketProtocol state = PacketProtocol.HANDSHAKE;

    public void upgradeConnection(PacketProtocol newState) {
        state = newState;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() == 0) {
            return;
        }

        try {
            int id = PacketUtils.readVarInt(in);
            Packet<?> packet = direction.getMapper(state).newPacket(id);

            if (packet == null) {
                throw new BadPacketException(id);
            }

            try {
                packet.read(in);
            } catch (Exception e) {
                throw new DecoderException(e);
            }

            out.add(packet);
        } finally {
            int remaining = in.readableBytes();

            if (remaining > 0) {
                in.skipBytes(remaining);
            }
        }
    }

}