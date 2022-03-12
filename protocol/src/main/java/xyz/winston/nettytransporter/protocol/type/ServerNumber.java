package xyz.winston.nettytransporter.protocol.type;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ServerNumber {

    private final int number;

    public boolean hasNumber() {
        return number != -1;
    }

}
