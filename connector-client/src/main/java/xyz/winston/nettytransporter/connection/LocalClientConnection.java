package xyz.winston.nettytransporter.connection;

import io.netty.channel.socket.SocketChannel;
import lombok.NonNull;
import xyz.winston.nettytransporter.ConnectorClient;
import xyz.winston.nettytransporter.protocol.channel.AbstractClientChannel;
import xyz.winston.nettytransporter.protocol.channel.AbstractRemoteServerChannel;
import xyz.winston.nettytransporter.protocol.conf.ClientConfiguration;

import java.net.SocketAddress;

public class LocalClientConnection extends AbstractClientChannel {

    private final ConnectorClient core;
    private final String serverName;

    public LocalClientConnection(
            final @NonNull ConnectorClient core,
            final @NonNull String serverName,
            final @NonNull SocketAddress address,
            final @NonNull ClientConfiguration clientConfiguration,
            final int threads
    ) {
        super(address, clientConfiguration, threads);

        this.core = core;
        this.serverName = serverName;
    }

    @Override
    protected AbstractRemoteServerChannel newServerChannel(SocketChannel channel) {
        return new RemoteServerConnection(core, serverName, this, channel);
    }

}
