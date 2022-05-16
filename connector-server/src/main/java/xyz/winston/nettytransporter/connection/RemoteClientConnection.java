package xyz.winston.nettytransporter.connection;

import io.netty.channel.socket.SocketChannel;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import xyz.winston.nettytransporter.ConnectorServer;
import xyz.winston.nettytransporter.connection.client.AbstractConnectable;
import xyz.winston.nettytransporter.connection.client.AbstractClient;
import xyz.winston.nettytransporter.connection.client.CommonClient;
import xyz.winston.nettytransporter.connection.client.ClientManager;
import xyz.winston.nettytransporter.protocol.channel.AbstractRemoteClientChannel;
import xyz.winston.nettytransporter.protocol.packet.ChannelProcessorContext;
import xyz.winston.nettytransporter.protocol.packet.PacketProcessor;
import xyz.winston.nettytransporter.protocol.packet.PacketProtocol;
import xyz.winston.nettytransporter.protocol.packet.handshake.Handshake;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author ItzStonlex (Скорее всего), Whilein, winston
 */
@Log4j2
public final class RemoteClientConnection extends AbstractRemoteClientChannel {

    // ------------------------------------------------------------------
    private static final ClientManager clientManager = ClientManager.IMP;
    // ------------------------------------------------------------------

    private final ConnectorServer core;

    @Getter
    private final String host;

    @Getter
    private int port;

    @Getter
    private String clientName;

    @Getter
    private AbstractConnectable client;

    public RemoteClientConnection(ConnectorServer core, SocketChannel channel) {
        super(channel);

        this.core = core;
        this.host = channel.remoteAddress().getHostString();
    }

    private String getDisplayName() {
        return client == null ? channel.remoteAddress().toString() : client.getName();
    }

    @Override
    protected void onDisconnect() {
        if (client == null) {
            log.info("Unknown client disconnected");
            return;
        }
        try {
            log.info("Client {} disconnected.", client.getName());
            ClientManager.IMP.removeClient((AbstractClient) client);
        } finally {
            client = null;
        }
    }

    @Override
    public void process(@NonNull Throwable throwable) {
        log.error("[{}] Unavailable to process packet", getDisplayName());
        throwable.printStackTrace();
    }

    @Override
    public void process(Handshake.Request packet, ChannelProcessorContext ctx) {
        // клиент пытается авторизоваться
        if (!packet.getToken().equals(core.getToken())) {
            ctx.setResponse(new Handshake.Response(Handshake.Result.FAILED, "Bad Token"));
            ctx.setShouldClose(true);
            return;
        }

        clientName = packet.getClientName();
        port = packet.getServerPort();

        if (ClientManager.IMP.hasClient(clientName)) {
            ctx.setResponse(new Handshake.Response(
                    Handshake.Result.FAILED,
                    "Client with that name already exists: " + clientName
            ));
            ctx.setShouldClose(true);
            return;
        }

        sendPacket(new Handshake.Response(Handshake.Result.SUCCESS, null));
        upgradeConnection(PacketProtocol.PLAY);

        createServer();
    }

    private void createServer() {
        client = new CommonClient(this, clientName);
        clientManager.addClient((AbstractClient) client);

        log.info("[{}] Registered new client", getDisplayName());
    }

}