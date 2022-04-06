package xyz.winston.nettytransporter;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import xyz.winston.nettytransporter.connection.Connection;
import xyz.winston.nettytransporter.connection.LocalClientConnection;
import xyz.winston.nettytransporter.protocol.exception.ConnectException;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.function.Consumer;

/**
 * @author winston
 */
@Getter
public class ConnectorClient {

    private final String host;
    private final int port;
    private final String token;

    private final String serverName;

    private LocalClientConnection connection;

    private final SocketAddress address;

    public ConnectorClient(
            final @NotNull String host,
            final int port,
            final @NotNull String serverName,
            final @NotNull String token
    ) {
        this.host = host;
        this.port = port;
        this.token = token;
        this.serverName = serverName;

        this.address = new InetSocketAddress(
                host,
                port
        );
    }

    public void openConnection(final @NotNull Runnable onConnected) {
        connection = new LocalClientConnection(this, serverName, address, 2);

        try {
            connection.connectSynchronized();
        } catch (ConnectException e) {
            e.printStackTrace();
        }

        onConnected.run();
        Connection.setConnection(connection);
    }
}
