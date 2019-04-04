package kr._19fstudy.effective_java.ch6.item39.exception_test;


public class ExceptionTestSample {
    @ExceptionTest(ArithmeticException.class)
    public static void m1() {
        int i = 0;
        i = i / i;
    }

    @ExceptionTest(ArithmeticException.class)
    public static void m2() {
        int[] a = new int[0];
        int i= a[1];
    }

    @ExceptionTest(ArithmeticException.class)
    public static void m3() {}
}
