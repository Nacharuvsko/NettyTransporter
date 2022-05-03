package xyz.winston.nettytransporter;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import xyz.winston.nettytransporter.connection.LocalServerConnection;
import xyz.winston.nettytransporter.protocol.exception.BindException;
import xyz.winston.nettytransporter.protocol.packet.*;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Главный класс коннектора сервера
 * @author winston
 */
@Getter
@Log4j2
public final class ConnectorServer {

    private static ConnectorServer instance;

    private final String token;

    private final ExecutorService executor;
    private final ScheduledExecutorService scheduler;

    private final long startDate = System.nanoTime();

    private LocalServerConnection connection;

    private final String host;
    private final int port;

    @Getter
    private final Collection<PacketProcessor> processors = new ArrayList<>();

    /**
     * @param port Порт, на котором будет запущен сервер
     * @param host Хост, на котором будет запущен сервер (невероятно)
     * @param token секретная ключ-строка, которая должна совпадать со строкой у клиента, дабы установилось соединение (как пароль :o)
     */
    public ConnectorServer(
            final @NonNull String host,
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

    public void openConnection() throws BindException {
        log.info("Opening connection...");

        connection = new LocalServerConnection(this,
                new InetSocketAddress(
                        host,
                        port
                ), 0, processors);

        connection.bindSynchronized();

        log.info("Connection opened at {}", connection.getChannel().localAddress());

        log.info("Using Epoll: {}", connection.isEpoll());
        log.info("Bootstrapped in {}ms", getUptime(TimeUnit.MILLISECONDS));
    }

    /**
     * Метод регистрации процессора
     */
    public void registerProcessor(final @NonNull PacketProcessor processor) {
        if(processors.contains(processor)) return;
        processors.add(processor);
    }

    public long getUptime(final @NotNull TimeUnit unit) {
        return unit.convert(System.nanoTime() - startDate, TimeUnit.NANOSECONDS);
    }

}
