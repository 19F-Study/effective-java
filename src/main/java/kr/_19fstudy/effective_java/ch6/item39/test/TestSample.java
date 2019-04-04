package kr._19fstudy.effective_java.ch6.item39.test;


public class TestSample {
    @Test
    public static void m1() {}

    public static void m2() {}

    @Test
    public static void m3() {
        throw new RuntimeException("failure");
    }

    public static void m4() {}

    @Test
    public void m5() {}

    public static void m6() {}

    @Test
    public static void m7() {
        throw new RuntimeException("failure");
    }

    public static void m8() {}
}
