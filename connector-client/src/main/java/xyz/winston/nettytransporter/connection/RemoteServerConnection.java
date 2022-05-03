package xyz.winston.nettytransporter.connection;


import io.netty.channel.socket.SocketChannel;
import lombok.extern.log4j.Log4j2;
import xyz.winston.nettytransporter.ConnectorClient;
import xyz.winston.nettytransporter.protocol.channel.AbstractRemoteServerChannel;
import xyz.winston.nettytransporter.protocol.packet.ChannelProcessorContext;
import xyz.winston.nettytransporter.protocol.packet.PacketProtocol;
import xyz.winston.nettytransporter.protocol.packet.handshake.Handshake;

@Log4j2
public class RemoteServerConnection extends AbstractRemoteServerChannel {

    private final ConnectorClient core;
    private final String serverName;

    public RemoteServerConnection(ConnectorClient core, String serverName,
                                  LocalClientConnection local, SocketChannel channel) {
        super(local, channel);

        this.core = core;
        this.serverName = serverName;
    }

    @Override
    public void active() {
        sendPacket(
                new Handshake.Request(
                        core.getToken(), serverName,
                        core.getPort()
                )
        );
    }

    @Override
    public void process(Handshake.Response packet, ChannelProcessorContext ctx) {
        if (packet.getResult() == Handshake.Result.FAILED) {
            log.error("[HANDSHAKE] Unable to connect to server: {}", packet.getMessage());
            return;
        }

        upgradeConnection(PacketProtocol.PLAY);

        log.info("[HANDSHAKE] Connection to server established");
    }
}
