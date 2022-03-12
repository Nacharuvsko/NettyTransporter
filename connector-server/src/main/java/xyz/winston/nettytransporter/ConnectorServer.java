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
        log.info("Открываем подключение...");

        this.connection = new LocalServerConnection(this,
                new InetSocketAddress(
                        host,
                        port
                ), 0);

        try {
            this.connection.bindSynchronized();
        } catch (BindException e) {
            log.trace("Не удалось открыть подключение", e);
            return;
        }

        log.info("Подключение открыто по адресу {}", connection.getChannel().localAddress());

        log.info("Использование Epoll: {}", connection.isEpoll());
        log.info("Ядро было запущено за {}мс", getUptime(TimeUnit.MILLISECONDS));
    }

    public long getUptime(final @NotNull TimeUnit unit) {
        return unit.convert(System.nanoTime() - startDate, TimeUnit.NANOSECONDS);
    }

}
