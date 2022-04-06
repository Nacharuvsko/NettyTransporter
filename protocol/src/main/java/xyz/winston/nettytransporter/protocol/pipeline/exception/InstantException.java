package xyz.winston.nettytransporter.protocol.pipeline.exception;

import org.jetbrains.annotations.NotNull;

/**
 * @author winston
 */
public class InstantException extends Exception {

    public InstantException(final @NotNull String message) {
        super(message);
    }

    public InstantException(
            final @NotNull String message,
            final @NotNull Throwable cause
    ) {
        super(message, cause);
    }

    public InstantException(final @NotNull Throwable cause) {
        super(cause);
    }

    @Override
    public Throwable initCause(final @NotNull Throwable cause) {
        return this;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
