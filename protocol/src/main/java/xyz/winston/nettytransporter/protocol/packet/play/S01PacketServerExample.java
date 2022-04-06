package xyz.winston.nettytransporter.protocol.packet.play;

import io.netty.buffer.ByteBuf;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import xyz.winston.nettytransporter.protocol.packet.ChannelProcessorContext;
import xyz.winston.nettytransporter.protocol.packet.Packet;
import xyz.winston.nettytransporter.protocol.packet.PacketProcessor;
import xyz.winston.nettytransporter.protocol.packet.PacketUtils;
import xyz.winston.nettytransporter.protocol.packet.play.processor.PlayClientProcessor;
import xyz.winston.nettytransporter.protocol.packet.play.processor.PlayServerProcessor;

/**
 * Пример пакета, отправляемого ОТ сервера КЛИЕНТУ
 * @author winston
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public final class S01PacketServerExample extends Packet<PlayServerProcessor> {

    private ExampleEnum exampleEnum;

    @Override
    public boolean isProcessor(PacketProcessor processor) {
        return processor instanceof PlayServerProcessor;
    }

    @Override
    protected void read0(@NotNull ByteBuf buf) {
        exampleEnum = PacketUtils.readEnum(buf, ExampleEnum.class);
    }

    @Override
    protected void write0(@NotNull ByteBuf buf) {
        PacketUtils.writeEnum(buf, exampleEnum);
    }

    @Override
    public void process(@NotNull PlayServerProcessor processor, @NotNull ChannelProcessorContext ctx) throws Exception {
        processor.process(this, ctx);
    }

    /** Funny packet enum example */
    private enum ExampleEnum {
        $_PRIVET_KAK_DELA_$
    }
}
