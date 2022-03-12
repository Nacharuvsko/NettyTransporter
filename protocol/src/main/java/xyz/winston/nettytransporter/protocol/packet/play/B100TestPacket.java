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
import xyz.winston.nettytransporter.protocol.packet.play.processor.PlayProcessor;

import java.util.UUID;

/**
 * Пример пакета, отправляемого по обе стороны
 * @author winston
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public final class B100TestPacket extends Packet<PlayProcessor> {

    private String message;

    @Override
    public void process(@NotNull PlayProcessor processor, @NotNull ChannelProcessorContext ctx) throws Exception {
        processor.process(this, ctx);
    }

    @Override
    public boolean isProcessor(PacketProcessor processor) {
        return processor instanceof PlayProcessor;
    }

    @Override
    public void read0(@NotNull ByteBuf buf) {
        message = PacketUtils.readString(buf);
    }

    @Override
    public void write0(@NotNull ByteBuf buf) {
        PacketUtils.writeString(buf, message);
    }

}
