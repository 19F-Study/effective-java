package kr._19fstudy.effective_java.ch5.item30.recursive_type_bound;

import java.util.Collection;
import java.util.Objects;

public class Main {

    public static <E extends Comparable<E>> E max(Collection<E> c) {
        if (c.isEmpty())
            throw new IllegalArgumentException("컬렉션이 비어 있습니다.");

        E result = null;
        for (E e : c)
            if (result == null || e.compareTo(result) > 0)
                result = Objects.requireNonNull(e);

        return result;
    }

}
