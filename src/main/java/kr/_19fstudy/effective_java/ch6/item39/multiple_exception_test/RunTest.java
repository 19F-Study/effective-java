package kr._19fstudy.effective_java.ch6.item39.multiple_exception_test;


import java.lang.reflect.Method;

public class RunTest {
    public static void main(String[] args) throws Exception {
        int tests = 0;
        int passed = 0;
        Class<?> testClass = MultipleExceptionTestSample.class;
        for (Method m: testClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(MultipleExceptionTest.class)) {
                tests++;
                try {
                    m.invoke(null);
                    System.out.printf("테스트 %s 실패: 예외를 던지지 않음%n", m);
                } catch (Throwable wrappedExc) {
                    Throwable exc = wrappedExc.getCause();
                    int oldPassed = passed;
                    Class<? extends Throwable>[] excTypes = m.getAnnotation(MultipleExceptionTest.class).value();
                    for (Class<? extends Throwable> excType: excTypes) {
                        if (excType.isInstance(exc)) {
                            passed++;
                            break;
                        }
                    }
                    if (passed == oldPassed) {
                        System.out.printf("테스트 %s 실패: %s %n", m, exc);
                    }
                }
            }
        }
        System.out.printf("성공: %d, 실패: %d%n", passed, tests - passed);
    }
}


