package xyz.winston.nettytransporter.protocol.packet;

/**
 * @author winston
 */
public enum PacketDirection {

    TO_SERVER {
        @Override
        public PacketMapper getMapper(PacketProtocol protocol) {
            return protocol.TO_SERVER;
        }
    },
    TO_CLIENT {
        @Override
        public PacketMapper getMapper(PacketProtocol protocol) {
            return protocol.TO_CLIENT;
        }
    }

    ;

    public abstract PacketMapper getMapper(PacketProtocol protocol);
}
