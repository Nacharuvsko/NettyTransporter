package xyz.winston.nettytransporter.connection.server;

import lombok.NonNull;
import ru.litecloud.core.connection.RemoteClientConnection;
import ru.litecloud.core.data.gamer.Gamer;
import ru.litecloud.core.protocol.logging.Logging;

public class Proxy extends AbstractConnectable {

    public Proxy(RemoteClientConnection connection, String name) {
        super(connection, name);
    }

    @Override
    public String toString() {
        return "Proxy/" + name;
    }

    @Override
    public boolean addPlayer(@NonNull Gamer gamer) {
        boolean added = gamers.add(gamer);

        if (added) {
            Logging.IMP.debug("{} подключен к {}", gamer.getName(), this);
        }

        return added;
    }

    @Override
    public boolean removePlayer(@NonNull Gamer gamer) {
        boolean removed = gamers.remove(gamer);

        if (removed) {
            Logging.IMP.debug("{} отключен от {}", gamer.getName(), this);
        }

        return removed;
    }
}
