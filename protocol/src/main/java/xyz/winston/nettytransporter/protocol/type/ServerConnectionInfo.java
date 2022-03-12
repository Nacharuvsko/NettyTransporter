package xyz.winston.nettytransporter.protocol.type;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import xyz.winston.nettytransporter.protocol.packet.PacketUtils;
import xyz.winston.nettytransporter.protocol.packet.serialize.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerConnectionInfo implements Serializable<ServerConnectionInfo> {

    private String serverName;
    private String serverAddress;
    private int serverPort;

    @Override
    public ServerConnectionInfo serialize(@NotNull ByteBuf out) {
        PacketUtils.writeString(out, serverName);
        PacketUtils.writeString(out, serverAddress);
        out.writeShort(serverPort);
        return this;
    }

    @Override
    public ServerConnectionInfo deserialize(@NotNull ByteBuf in) {
        serverName = PacketUtils.readString(in);
        serverAddress = PacketUtils.readString(in);
        serverPort = in.readUnsignedShort();
        return this;
    }
}
