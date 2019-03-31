package kr._19fstudy.effective_java.ch6.item39.repeatable_test;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(RepeatableExceptionTestContainer.class)
public @interface RepeatableExceptionTest {
    Class<? extends Throwable> value();
}
