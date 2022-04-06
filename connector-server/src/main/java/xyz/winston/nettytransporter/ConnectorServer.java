package xyz.winston.nettytransporter;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import xyz.winston.nettytransporter.connection.LocalServerConnection;
import xyz.winston.nettytransporter.protocol.exception.BindException;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Главный класс коннектора сервера
 * @author winston
 */
@Log4j2
public final class ConnectorServer {

    @Getter
    private static ConnectorServer instance;

    @Getter
    private final String token;

    private final ExecutorService executor;
    private final ScheduledExecutorService scheduler;

    private final long startDate = System.nanoTime();

    @Getter
    private LocalServerConnection connection;

    private final String host;
    private final int port;

    /**
     * @param port Порт, на котором будет запущен сервер
     */
    public ConnectorServer(
            final String host,
            final int port,
            final @NotNull String token
    ) {
        instance = this;

        this.host = host;
        this.port = port;
        this.token = token;

        scheduler = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
        executor = Executors.newCachedThreadPool();
    }

    public void openConnection() {
        log.info("Opening connection...");

        this.connection = new LocalServerConnection(this,
                new InetSocketAddress(
                        host,
                        port
                ), 0);

        try {
            this.connection.bindSynchronized();
        } catch (BindException e) {
            log.trace("Unavailable to open connection", e);
            return;
        }

        log.info("Connection opened at {}", connection.getChannel().localAddress());

        log.info("Using Epoll: {}", connection.isEpoll());
        log.info("Bootstrapped in {}ms", getUptime(TimeUnit.MILLISECONDS));
    }

    public long getUptime(final @NotNull TimeUnit unit) {
        return unit.convert(System.nanoTime() - startDate, TimeUnit.NANOSECONDS);
    }

}
