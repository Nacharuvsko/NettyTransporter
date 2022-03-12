package xyz.winston.nettytransporter.protocol.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.RequiredArgsConstructor;
import xyz.winston.nettytransporter.protocol.packet.Packet;
import xyz.winston.nettytransporter.protocol.packet.PacketDirection;
import xyz.winston.nettytransporter.protocol.packet.PacketProtocol;
import xyz.winston.nettytransporter.protocol.packet.PacketUtils;

@RequiredArgsConstructor
public class PacketEncoder extends MessageToByteEncoder<Packet<?>> {

    private final PacketDirection direction;
    private PacketProtocol state = PacketProtocol.HANDSHAKE;

    public void upgradeConnection(PacketProtocol newState) {
        state = newState;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet<?> packet, ByteBuf buf) throws Exception {
        int packetId = direction.getMapper(state).getPacketId(packet.getClass());

        if (packetId == -1) {
            throw new EncoderException("Tried to send unregistered packet: [Packet: " + packet + ", State: " + state + "]");
        }

        PacketUtils.writeVarInt(buf, packetId);
        packet.write(buf);
    }
}