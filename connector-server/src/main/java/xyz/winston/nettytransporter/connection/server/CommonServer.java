package xyz.winston.nettytransporter.connection.server;

import lombok.Setter;
import xyz.winston.nettytransporter.connection.RemoteClientConnection;

public class CommonServer extends AbstractServer {

    @Setter
    protected volatile boolean joinable;

    public CommonServer(RemoteClientConnection connection, String name) {
        super(connection, name);
    }

    @Override
    public String toString() {
        return "Common/" + name;
    }
}
