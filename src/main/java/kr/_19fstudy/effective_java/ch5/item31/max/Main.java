package kr._19fstudy.effective_java.ch5.item31.max;

import java.util.List;
import java.util.Objects;

public class Main {

    // origin signature
    // public static <E extends Comparable<E>> E max(Collection<E> c)
    public static <E extends Comparable<? super E>> E max(List<? extends E> c) {
        if (c.isEmpty())
            throw new IllegalArgumentException("컬렉션이 비어 있습니다.");

        E result = null;

        // 입력 매개변수에서는 E 인스턴스를 생산
        for (E e : c)
            // Comparable<E>는 E 인스턴스를 소비한다. 그래서 Comparable<? super E>로
            if (result == null || e.compareTo(result) > 0)
                result = Objects.requireNonNull(e);

        return result;
    }

}
