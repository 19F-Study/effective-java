package kr._19fstudy.effective_java.ch6.item39;

import junit.framework.TestCase;


/**
 * This class will be failed to build by gradle 4.x.
 * https://github.com/gradle/gradle/issues/7059
 */
public class JUnit3Test extends TestCase {

    public void testA() { }

    public void A() { }

    public void testRuntimeException() throws Exception {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        String exceptionString = methodName.replace("test", "");
        Class exceptionClass = Class.forName("java.lang." + exceptionString);
        Exception expected = (Exception) exceptionClass.newInstance();
        Exception response = new RuntimeException();
        assertEquals(expected.getClass().getName(), response.getClass().getName());
    }
}