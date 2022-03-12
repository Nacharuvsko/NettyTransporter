package xyz.winston.nettytransporter.connection;

import io.netty.channel.socket.SocketChannel;
import xyz.winston.nettytransporter.ConnectorClient;
import xyz.winston.nettytransporter.protocol.channel.AbstractClientChannel;
import xyz.winston.nettytransporter.protocol.channel.AbstractRemoteServerChannel;

import java.net.SocketAddress;

public class LocalClientConnection extends AbstractClientChannel {

    private final ConnectorClient core;
    private final String serverName;

    public LocalClientConnection(ConnectorClient core, String serverName, SocketAddress address, int threads) {
        super(address, threads);

        this.core = core;
        this.serverName = serverName;
    }

    @Override
    protected AbstractRemoteServerChannel newServerChannel(SocketChannel channel) {
        return new RemoteServerConnection(core, serverName, this, channel);
    }

}
