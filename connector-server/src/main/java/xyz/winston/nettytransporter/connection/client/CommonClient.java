package xyz.winston.nettytransporter.connection.client;

import org.jetbrains.annotations.NotNull;
import xyz.winston.nettytransporter.connection.RemoteClientConnection;

public class CommonClient extends AbstractClient {

    public CommonClient(final @NotNull RemoteClientConnection connection, String name) {
        super(connection, name);
    }

    @Override
    public String toString() {
        return "Common/" + name;
    }
}
