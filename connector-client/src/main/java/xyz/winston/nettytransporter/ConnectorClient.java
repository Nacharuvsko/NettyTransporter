package xyz.winston.nettytransporter;

import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.winston.nettytransporter.protocol.conf.ClientConfiguration;
import xyz.winston.nettytransporter.connection.Connection;
import xyz.winston.nettytransporter.connection.LocalClientConnection;
import xyz.winston.nettytransporter.protocol.exception.ConnectException;
import xyz.winston.nettytransporter.protocol.packet.PacketProcessor;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * @author winston
 */
@Getter
public final class ConnectorClient {

    private final String host;
    private final int port;
    private final String token;

    private final String serverName;

    private final ClientConfiguration clientConfiguration;

    private LocalClientConnection connection;

    private final SocketAddress address;

    public ConnectorClient(
            final @NotNull String host,
            final int port,
            final @NotNull String clientName,
            final @NotNull String token
    ) {
        this.host = host;
        this.port = port;
        this.token = token;
        this.serverName = clientName;

        this.address = new InetSocketAddress(
                host,
                port
        );

        this.clientConfiguration = new ClientConfiguration();
    }

    public void openConnection() throws ConnectException{
        openConnection(null);
    }

    public void openConnection(final @Nullable Runnable onConnected) throws ConnectException{
        connection = new LocalClientConnection(this, serverName, address, clientConfiguration, 2);
        connection.connectSynchronized();

        Connection.setConnection(connection);

        if (onConnected != null) {
            onConnected.run();
        }
    }

    public void registerProcessor(final @NonNull PacketProcessor processor) {
        Connection.registerProcessor(processor);
    }
}
