package xyz.winston.nettytransporter.protocol.conf;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public final class ClientConfiguration {

    private boolean autoReconnect;
    private Runnable disconnectAction;

}
