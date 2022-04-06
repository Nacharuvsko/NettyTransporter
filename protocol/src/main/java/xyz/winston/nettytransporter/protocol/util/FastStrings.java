package xyz.winston.nettytransporter.protocol.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

/**
 * @author WhileIn
 */
@UtilityClass
public final class FastStrings {

    protected final static ThreadLocal<@NotNull StringBuilder> CACHE = ThreadLocal.withInitial(StringBuilder::new);

    public static @NotNull StringBuilder getEmptyBuilder() {
        StringBuilder sb = CACHE.get();
        sb.setLength(0);
        return sb;
    }

}
