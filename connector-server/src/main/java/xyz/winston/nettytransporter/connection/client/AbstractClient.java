package xyz.winston.nettytransporter.connection.client;

import lombok.Getter;
import lombok.Setter;
import xyz.winston.nettytransporter.connection.RemoteClientConnection;
import xyz.winston.nettytransporter.protocol.type.ServerConnectionInfo;

@Setter
@Getter
public abstract class AbstractClient extends AbstractConnectable {

    protected int slots;

    public AbstractClient(RemoteClientConnection connection, String name) {
        super(connection, name);
    }

    public ServerConnectionInfo getInfo() {
        return new ServerConnectionInfo(name, getHost(), getPort());
    }

    public String getHost() {
        return connection.getHost();
    }

    public int getPort() {
        return connection.getPort();
    }

    @Override
    public String toString() {
        return "AbstractServer/" + getName();
    }

}
