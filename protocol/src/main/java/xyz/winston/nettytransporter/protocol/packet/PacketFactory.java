package xyz.winston.nettytransporter.protocol.packet;

/**
 * @author winston
 */
public interface PacketFactory {

    Packet<?> newInstance();

}
