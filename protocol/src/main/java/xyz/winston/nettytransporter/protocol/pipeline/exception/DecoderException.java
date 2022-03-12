package xyz.winston.nettytransporter.protocol.pipeline.exception;

/**
 * @author winston
 */
public class DecoderException extends InstantException {

    public DecoderException(String message) {
        super(message);
    }

    public DecoderException(Throwable cause) {
        super(cause);
    }

}
