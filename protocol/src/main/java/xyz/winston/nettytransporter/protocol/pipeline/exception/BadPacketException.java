package xyz.winston.nettytransporter.protocol.pipeline.exception;

/**
 * @author winston
 */
public class BadPacketException extends InstantException {

    public BadPacketException(int packetId) {
        super("Bad Packet: [ID: " + packetId + "]");
    }
}
