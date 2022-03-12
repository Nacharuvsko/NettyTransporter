package xyz.winston.nettytransporter.connection.server;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import xyz.winston.nettytransporter.protocol.packet.Packet;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ServerManager {

    // =========================================================
    public static final ServerManager IMP = new ServerManager();
    // =========================================================

    private final Map<String, AbstractServer> servers
            = new HashMap<>();

    private final Map<String, CompletableFuture<AbstractServer>> serverFutures
            = new HashMap<>();

    public CompletableFuture<AbstractServer> getFutureServer(@NotNull String name) {
        AbstractServer server = getServer(name);

        if (server != null) {
            return CompletableFuture.completedFuture(server);
        }

        synchronized (serverFutures) {
            return serverFutures.computeIfAbsent(name.toLowerCase(), x -> new CompletableFuture<>());
        }
    }

    public boolean hasServer(String name) {
        synchronized (servers) {
            return servers.containsKey(name.toLowerCase());
        }
    }

    public AbstractServer getServer(@NonNull String name) {
        synchronized (servers) {
            return servers.get(name.toLowerCase());
        }
    }

    public int getServerCount() {
        synchronized (servers) {
            return servers.size();
        }
    }

    public Collection<AbstractServer> getServers() {
        synchronized (servers) {
            return Collections.unmodifiableCollection(new HashSet<>(servers.values()));
        }
    }

    public Set<String> findServerNames(@NonNull String regexp) {
        Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);

        synchronized (servers) {
            return servers.keySet().stream()
                    .filter(serverName -> pattern.matcher(serverName).matches())
                    .collect(Collectors.toSet());
        }
    }

    public Set<AbstractServer> findServers(@NonNull String regexp) {
        Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);

        synchronized (servers) {
            return servers.values().stream()
                    .filter(server -> pattern.matcher(server.getName()).matches())
                    .collect(Collectors.toSet());
        }
    }

    public void addServer(@NonNull AbstractServer server) {
        String serverName = server.getName().toLowerCase();

        synchronized (servers) {
            servers.put(serverName, server);
        }

        synchronized (serverFutures) {
            CompletableFuture<AbstractServer> serverFuture = serverFutures.remove(serverName);

            if (serverFuture != null) {
                serverFuture.complete(server);
            }
        }
    }


    public void removeServer(@NonNull AbstractServer server) {
        String serverName = server.getName().toLowerCase();

        synchronized (servers) {
            servers.remove(serverName);
        }
    }

    public void sendToServers(@NonNull Packet<?> packet) {
        synchronized (servers) {
            for (AbstractServer server : servers.values()) {
                server.sendPacket(packet);
            }
        }
    }

}