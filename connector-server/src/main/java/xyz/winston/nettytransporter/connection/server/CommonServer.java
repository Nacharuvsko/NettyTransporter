package xyz.winston.nettytransporter.connection.server;

import org.jetbrains.annotations.NotNull;
import xyz.winston.nettytransporter.connection.RemoteClientConnection;

public class CommonServer extends AbstractServer {

    public CommonServer(final @NotNull RemoteClientConnection connection, String name) {
        super(connection, name);
    }

    @Override
    public String toString() {
        return "Common/" + name;
    }
}
