package xyz.winston.nettytransporter;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

@Log4j2
public final class ConnectorServerTest {

    private final ConnectorServer server;

    public ConnectorServerTest() {
        server = new ConnectorServer("127.0.0.1", 1337, "sosi");
        server.openConnection();
    }

    public static void main(final String @NotNull [] args) {
        new ConnectorServerTest();
    }

}