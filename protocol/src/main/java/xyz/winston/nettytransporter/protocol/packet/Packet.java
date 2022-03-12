package xyz.winston.nettytransporter.protocol.packet;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

/**
 * @param <Processor> Процессор пакетов
 * @author winston
 */
public abstract class Packet<Processor extends PacketProcessor> {

    public abstract boolean isProcessor(PacketProcessor processor);

    protected abstract void read0(@NotNull ByteBuf buf) throws Exception;
    protected abstract void write0(@NotNull ByteBuf buf) throws Exception;

    public void write(@NotNull ByteBuf buf) throws Exception {
        write0(buf);
    }

    public void read(@NotNull ByteBuf buf) throws Exception {
        read0(buf);
    }

    public abstract void process(@NotNull Processor processor, @NotNull ChannelProcessorContext ctx) throws Exception;

    public static abstract class Response<Processor extends PacketProcessor> extends Identifiable<Processor> {

    }

    public static abstract class Request<
            A extends PacketProcessor,
            B extends Response<?>> extends Identifiable<A> {

        public abstract Class<B> getResponse();

    }

    public static abstract class Identifiable<Processor extends PacketProcessor> extends Packet<Processor> {
        private int requestId;

        public int getRequestId() {
            return requestId;
        }

        public void setRequestId(int requestId) {
            this.requestId = requestId;
        }

        public boolean hasRequestId() {
            return requestId != 0;
        }

        public void read(@NotNull ByteBuf buf) throws Exception {
            super.read(buf);

            if (buf.readableBytes() > 0) {
                requestId = PacketUtils.readVarInt(buf);
            }
        }

        public void write(@NotNull ByteBuf buf) throws Exception {
            super.write(buf);

            if (hasRequestId()) {
                PacketUtils.writeVarInt(buf, requestId);
            }
        }

    }

}