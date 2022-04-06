package xyz.winston.nettytransporter.protocol.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

/**
 * @param <T> Тип элементов в массиве
 * @author WhileIn
 */
public final class ArrayIterator<T> implements Iterator<T> {

    private final T[] array;

    private int position;

    public ArrayIterator(final @NotNull T[] array) {
        this.array = array;
    }

    @Override
    public boolean hasNext() {
        return position != array.length;
    }

    @Override
    public @Nullable T next() {
        return array[position++];
    }

}
