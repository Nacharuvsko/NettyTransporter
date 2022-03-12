package xyz.winston.nettytransporter.protocol.packet.serialize;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

public interface Serializable<T extends Serializable<T>> {

    T serialize(final @NotNull ByteBuf out);

    T deserialize(final @NotNull ByteBuf in);

}
