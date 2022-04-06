package xyz.winston.nettytransporter.connection;

import com.sun.jdi.connect.Connector;
import io.netty.channel.socket.SocketChannel;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import xyz.winston.nettytransporter.ConnectorServer;
import xyz.winston.nettytransporter.connection.server.AbstractConnectable;
import xyz.winston.nettytransporter.connection.server.AbstractServer;
import xyz.winston.nettytransporter.connection.server.CommonServer;
import xyz.winston.nettytransporter.connection.server.ServerManager;
import xyz.winston.nettytransporter.protocol.channel.AbstractRemoteClientChannel;
import xyz.winston.nettytransporter.protocol.packet.ChannelProcessorContext;
import xyz.winston.nettytransporter.protocol.packet.PacketProtocol;
import xyz.winston.nettytransporter.protocol.packet.handshake.Handshake;

@Log4j2
public class RemoteClientConnection extends AbstractRemoteClientChannel {

    private static final ServerManager serverManager = ServerManager.IMP;

    private final ConnectorServer core;

    @Getter
    private final String host;

    @Getter
    private int port;

    @Getter
    private String serverName;

    @Getter
    private AbstractConnectable server;

    public RemoteClientConnection(ConnectorServer core, SocketChannel channel) {
        super(channel);

        this.core = core;
        this.host = channel.remoteAddress().getHostString();
    }

    private String getDisplayName() {
        return server == null ? channel.remoteAddress().toString() : server.getName();
    }

    @Override
    public void active() {
        // core.getPluginManager().callEvent(new ClientActiveEvent(this));
    }

    @Override
    protected void onDisconnect() {
        try {
            log.info("Server {} disconnected.", server.getName());
            ServerManager.IMP.removeServer((AbstractServer) server);
        } finally {
            server = null;
        }
    }

    @Override
    public void process(@NonNull Throwable throwable) {
        log.error("[{}] Unavailable to process packet", getDisplayName());
        log.trace(throwable);
        throwable.printStackTrace();
    }

    @Override
    public void process(Handshake.Request packet, ChannelProcessorContext ctx) {
        // клиент пытается авторизоваться
        if (!packet.getToken().equals(core.getToken())) {
            ctx.setResponse(new Handshake.Response(Handshake.Result.FAILED, "Bad Token"));
            close();
            return;
        }

        boolean isProxy = packet.getType() == Handshake.ConnectionType.PROXY;

        serverName = packet.getServerName();
        port = packet.getServerPort();

        if (ServerManager.IMP.hasServer(serverName)) {
            ctx.setResponse(new Handshake.Response(
                    Handshake.Result.FAILED,
                    "Server with that name already exists: " + serverName
            ));
            close();
            return;
        }

        sendPacket(new Handshake.Response(Handshake.Result.SUCCESS, null));
        upgradeConnection(PacketProtocol.PLAY);

        // баккит должен будет отправить слоты и карту (если это мини-игра)
        // для прокси сразу в плей

        createServer();
    }

    private void createServer() {
        server = new CommonServer(this, serverName);
        serverManager.addServer((AbstractServer) server);

        log.info("[{}] Registered new server", getDisplayName());
    }

}