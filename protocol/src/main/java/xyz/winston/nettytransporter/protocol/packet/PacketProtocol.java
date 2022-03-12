package xyz.winston.nettytransporter.protocol.packet;

import xyz.winston.nettytransporter.protocol.packet.handshake.Handshake;
import xyz.winston.nettytransporter.protocol.packet.play.*;

public enum PacketProtocol {

    HANDSHAKE {
        {
            toServer(0, Handshake.Request.class, Handshake.Request::new);
            toClient(0, Handshake.Response.class, Handshake.Response::new);
        }
    },
    PLAY {
        {
            both(100, B100TestPacket.class, B100TestPacket::new);

            toClient(1, C01PacketClientExample.class, C01PacketClientExample::new);

            toServer(1, S01PacketServerExample.class, S01PacketServerExample::new);
        }
    };

    public final PacketMapper TO_SERVER = new PacketMapper(), TO_CLIENT = new PacketMapper();

    public <T extends Packet<?>> void toServer(int id, Class<?> cls, PacketFactory factory) {
        TO_SERVER.registerPacket(id, cls, factory);
    }

    public <T extends Packet<?>> void toClient(int id, Class<?> cls, PacketFactory factory) {
        TO_CLIENT.registerPacket(id, cls, factory);
    }

    public <T extends Packet<?>> void both(int id, Class<?> cls, PacketFactory factory) {
        toServer(id, cls, factory);
        toClient(id, cls, factory);
    }

}