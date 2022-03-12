package xyz.winston.nettytransporter.protocol.exception;

import org.jetbrains.annotations.NotNull;
import xyz.winston.nettytransporter.protocol.channel.AbstractChannel;

/**
 * @author winston
 */
public class BindException extends Exception {

    public BindException(final @NotNull AbstractChannel channel, final @NotNull Throwable cause) {
        super("Unable to bind server [" + channel.getSocketAddress() + "]", cause);
    }

}
