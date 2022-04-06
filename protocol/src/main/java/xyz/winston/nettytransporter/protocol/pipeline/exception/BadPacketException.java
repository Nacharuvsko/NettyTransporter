package xyz.winston.nettytransporter.protocol.pipeline.exception;

/**
 * @author winston
 */
public final class BadPacketException extends InstantException {

    public BadPacketException(int packetId) {
        super("Bad Packet: [ID: " + packetId + "]");
    }
}
