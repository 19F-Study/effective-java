package kr._19fstudy.effective_java.ch6.item39.multiple_exception_test;


import java.util.ArrayList;
import java.util.List;

public class MultipleExceptionTestSample {
    @MultipleExceptionTest({IndexOutOfBoundsException.class, NullPointerException.class})
    public static void doubleyBad() {
        List<String> list = new ArrayList<>();
        // 자바 API 명세에 따르면 다음 메서드는 IndexOutOfBoundsException 이나,
        // NullPointerException을 던질 수 있다.
        list.addAll(5, null);
    }
}
