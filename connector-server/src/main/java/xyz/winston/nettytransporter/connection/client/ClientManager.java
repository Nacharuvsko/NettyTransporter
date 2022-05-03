package xyz.winston.nettytransporter.connection.client;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import xyz.winston.nettytransporter.protocol.packet.Packet;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ClientManager {

    // =========================================================
    public static final ClientManager IMP = new ClientManager();
    // =========================================================

    private final Map<String, AbstractClient> clients
            = new HashMap<>();

    private final Map<String, CompletableFuture<AbstractClient>> clientFutures
            = new HashMap<>();

    public CompletableFuture<AbstractClient> getFutureClient(@NotNull String name) {
        AbstractClient client = getClient(name);

        if (client != null) {
            return CompletableFuture.completedFuture(client);
        }

        synchronized (clientFutures) {
            return clientFutures.computeIfAbsent(name.toLowerCase(), x -> new CompletableFuture<>());
        }
    }

    public boolean hasClient(String name) {
        synchronized (clients) {
            return clients.containsKey(name.toLowerCase());
        }
    }

    public AbstractClient getClient(@NonNull String name) {
        synchronized (clients) {
            return clients.get(name.toLowerCase());
        }
    }

    public int getClientsCount() {
        synchronized (clients) {
            return clients.size();
        }
    }

    public Collection<AbstractClient> getClients() {
        synchronized (clients) {
            return Collections.unmodifiableCollection(new HashSet<>(clients.values()));
        }
    }

    public Set<String> findClientsNames(@NonNull String regexp) {
        Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);

        synchronized (clients) {
            return clients.keySet().stream()
                    .filter(clientName -> pattern.matcher(clientName).matches())
                    .collect(Collectors.toSet());
        }
    }

    public Set<AbstractClient> findClients(@NonNull String regexp) {
        Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);

        synchronized (clients) {
            return clients.values().stream()
                    .filter(client -> pattern.matcher(client.getName()).matches())
                    .collect(Collectors.toSet());
        }
    }

    public void addClient(@NonNull AbstractClient client) {
        String clientName = client.getName().toLowerCase();

        synchronized (clients) {
            clients.put(clientName, client);
        }

        synchronized (clientFutures) {
            CompletableFuture<AbstractClient> clientFuture = clientFutures.remove(clientName);

            if (clientFuture != null) {
                clientFuture.complete(client);
            }
        }
    }


    public void removeClient(@NonNull AbstractClient client) {
        String clientName = client.getName().toLowerCase();

        synchronized (clients) {
            clients.remove(clientName);
        }
    }

    public void sendToClients(@NonNull Packet<?> packet) {
        synchronized (clients) {
            for (AbstractClient client : clients.values()) {
                client.sendPacket(packet);
            }
        }
    }

}