package xyz.winston.nettytransporter.protocol.packet.handshake;

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
import xyz.winston.nettytransporter.protocol.packet.handshake.processor.HandshakeClientProcessor;
import xyz.winston.nettytransporter.protocol.packet.handshake.processor.HandshakeServerProcessor;

/**
 * Пакет для установки соединения
 *
 * @author winston
 */
public class Handshake {

    private Handshake() {
        throw new UnsupportedOperationException();
    }

    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Request extends Packet.Request<HandshakeClientProcessor, Response> {

        /**
         * Токен - кодовая строка, которая должна совпасть с токеном прописанным в
         * конфигурации сервера, в ином случае соединение не установится
         */
        private String token;

        private String clientName;
        private int serverPort;

        @Override
        public void process(@NotNull HandshakeClientProcessor processor, @NotNull ChannelProcessorContext ctx) throws Exception {
            processor.process(this, ctx);
        }

        @Override
        public boolean isProcessor(PacketProcessor processor) {
            return processor instanceof HandshakeClientProcessor;
        }

        @Override
        public void read0(@NotNull ByteBuf buf) {
            token = PacketUtils.readString(buf);
            clientName = PacketUtils.readString(buf);
            serverPort = buf.readUnsignedShort();
        }

        @Override
        public void write0(@NotNull ByteBuf buf) {
            PacketUtils.writeString(buf, token);
            PacketUtils.writeString(buf, clientName);
            buf.writeShort(serverPort);
        }

        @Override
        public Class<Handshake.Response> getResponse() {
            return Handshake.Response.class;
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @SuppressWarnings("unused")
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Response extends Packet.Response<HandshakeServerProcessor> {

        private Result result;
        private String message;

        public boolean isSuccess() {
            return result == Result.SUCCESS;
        }

        @Override
        public void process(@NotNull HandshakeServerProcessor processor, @NotNull ChannelProcessorContext ctx) throws Exception {
            processor.process(this, ctx);
        }

        @Override
        public boolean isProcessor(PacketProcessor processor) {
            return processor instanceof HandshakeServerProcessor;
        }

        @Override
        public void read0(@NotNull ByteBuf buf) {
            result = PacketUtils.readEnum(buf, Result.class);

            if (result == Result.FAILED) {
                message = PacketUtils.readString(buf);
            }
        }

        @Override
        public void write0(@NotNull ByteBuf buf) {
            PacketUtils.writeEnum(buf, result);

            if (result == Result.FAILED) {
                PacketUtils.writeString(buf, message);
            }
        }
    }

    public enum Result {
        FAILED, SUCCESS
    }
}
