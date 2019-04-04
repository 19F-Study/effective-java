package kr._19fstudy.effective_java.ch6.item38;

import java.util.Collection;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        test(ExtendedOperation.class, 4.4, 5.5);
        test2(List.of(ExtendedOperation.values()), 4.4, 5.5);
    }

    // <T extends Enum<T> & Operation> : Class 객체가 열거 타입인 동시에 Operation 의 하위 타입이어야 한다는 뜻.
    private static <T extends Enum<T> & Operation> void test(Class<T> opEnumType,
                                                             double x,
                                                             double y) {
        for (Operation op : opEnumType.getEnumConstants()) {
            System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
        }
    }

    private static void test2(Collection<? extends Operation> opSet,
                              double x,
                              double y) {
        for (Operation op : opSet) {
            System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
        }
    }

}
