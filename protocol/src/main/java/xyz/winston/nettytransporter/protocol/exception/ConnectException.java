package xyz.winston.nettytransporter.protocol.exception;

import org.jetbrains.annotations.NotNull;
import xyz.winston.nettytransporter.protocol.channel.AbstractChannel;

/**
 * @author winston
 */
public class ConnectException extends Exception {

    public ConnectException(final @NotNull AbstractChannel channel, final @NotNull Throwable cause) {
        super("Unable to connect to server [" + channel.getSocketAddress() + "]", cause);
    }

}
