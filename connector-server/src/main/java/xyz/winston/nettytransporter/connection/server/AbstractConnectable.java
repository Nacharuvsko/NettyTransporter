package xyz.winston.nettytransporter.connection.server;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import xyz.winston.nettytransporter.connection.RemoteClientConnection;
import xyz.winston.nettytransporter.protocol.packet.Packet;

@Getter
@RequiredArgsConstructor
public abstract class AbstractConnectable {

    protected final RemoteClientConnection connection;

    protected final String name;

    public void close() {
        connection.close();
    }

    public void sendPacket(@NonNull Packet packet) {
        connection.sendPacket(packet);
    }

    public int hashCode() {
        return name.hashCode();
    }

}
