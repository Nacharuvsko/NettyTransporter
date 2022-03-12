package xyz.winston.nettytransporter.protocol.packet.play;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import xyz.winston.nettytransporter.protocol.packet.ChannelProcessorContext;
import xyz.winston.nettytransporter.protocol.packet.Packet;
import xyz.winston.nettytransporter.protocol.packet.PacketProcessor;
import xyz.winston.nettytransporter.protocol.packet.PacketUtils;
import xyz.winston.nettytransporter.protocol.packet.play.processor.PlayClientProcessor;

/**
 * Пример пакета, отправляемого клиенту
 * @author winston
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public final class C01PacketClientExample extends Packet<PlayClientProcessor> {

    private String message;
    private int integer;
    private byte someByte;

    @Override
    public void process(@NotNull PlayClientProcessor processor, @NotNull ChannelProcessorContext ctx) throws Exception {
        processor.process(this, ctx);
    }

    @Override
    public boolean isProcessor(PacketProcessor processor) {
        return processor instanceof PlayClientProcessor;
    }

    @Override
    public void read0(@NotNull ByteBuf buf) {
        message = PacketUtils.readString(buf);
        integer = buf.readInt();
        someByte = buf.readByte();
    }

    @Override
    public void write0(@NotNull ByteBuf buf) {
        PacketUtils.writeString(buf, message);
        buf.writeInt(integer);
        buf.writeByte(someByte);
    }

}
