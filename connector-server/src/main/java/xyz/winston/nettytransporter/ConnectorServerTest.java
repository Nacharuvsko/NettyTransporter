package xyz.winston.nettytransporter;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Log4j2
public final class ConnectorServerTest {

    private final ConnectorServer server;
    private final ScheduledExecutorService singleExecutor = Executors.newSingleThreadScheduledExecutor();

    public ConnectorServerTest() {
        server = new ConnectorServer("127.0.0.1", 1337, "sosi");
        server.openConnection();
        /*
            singleExecutor.scheduleAtFixedRate(() -> {
                log.info("Sent le packet $");
                ServerManager.IMP.sendToServers(new B100TestPacket("$ Cock $"));
            }, 0, 1, TimeUnit.SECONDS);
        */
    }

    public static void main(final String @NotNull [] args) {
        new ConnectorServerTest();
    }

}