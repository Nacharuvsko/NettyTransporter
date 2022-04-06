package xyz.winston.nettytransporter.protocol.pipeline.exception;

import org.jetbrains.annotations.NotNull;

/**
 * @author winston
 */
public final class DecoderException extends InstantException {

    public DecoderException(final @NotNull String message) {
        super(message);
    }

    public DecoderException(final @NotNull Throwable cause) {
        super(cause);
    }

}
