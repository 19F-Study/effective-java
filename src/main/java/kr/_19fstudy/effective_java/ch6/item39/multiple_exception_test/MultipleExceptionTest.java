package kr._19fstudy.effective_java.ch6.item39.multiple_exception_test;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MultipleExceptionTest {
    Class<? extends Throwable>[] value();
}
