package xyz.winston.nettytransporter;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

/**
 * Example of {@link ConnectorClient client}
 */
@Log4j2
public final class ConnectorClientTest {

    private final ConnectorClient client;

    /** Synthetic */
    public ConnectorClientTest() {
        client = new ConnectorClient("127.0.0.1", 1337, "Client-Example", "sosi");
        client.openConnection(() -> log.info("Connection established!"));
    }

    public static void main(final String @NotNull [] args) {
        new ConnectorClientTest();
    }

}