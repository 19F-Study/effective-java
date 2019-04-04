# Item 39. 명명 패턴보다 애너테이션을 사용하라


## 명명 패턴
전통적으로 도구나 프레임워크가 특별히 다뤄야 할 프로그램 요소에는 명명패턴을 사용해왔다. 일례로 테스트 프레임워크인 JUnit은 버전3까지 테스트 메서드 이름을 test로 시작하게끔 했다. 효과적인 방법이지만, 몇 가지 단점이 있다.
1.  **오타가 나면 안된다.** 실수로 이름을 tsetXXX 라고 지으면 JUnit 3은 이 메서드를 무시하고 지나치기 때문에 개발자는 이 테스트가 통과했다고 오해할 수 있다.
2.  **올바른 프로그램 요소에서만 사용되리라 보장할 수 없다.** 예를 들어 클래스 이름을 TestSafetyMechanisms로 지어서 JUnit에게 실행하도록 해도, JUnit은 클래스 이름에는 관심이 없다. JUnit은 경고 메세지조차 출력하지 않고 테스트 또한 전혀 수행되지 않을 것이다.
3.  **프로그램 요소를 매개변수로 전달할 마땅한 방법이 없다.** 특정 예외를 던져야만 성공하는 테스트가 있다고 해보자. 기대하는 예외 타입을 테스트에 매개변수로 전달해야 하는 상황이다. 예외의 이름을 테스트 메서드 이름에 덧붙이는 방법도 있지만, 보기도 나쁘고 깨지기도 쉽다. 컴파일러는 메서드 이름에 덧붙인 문자열이 예외를 가리키는지, 그런 클래스가 존재는 하는지조차 알 수 없다.
```java
public class JUnit3Test extends TestCase {  
    public void testA() {}    
    
    public void A() {}  
    
    public void testRuntimeException() throws Exception {  
        String methodName = new Object({}.getClass().getEnclosingMethod().getName();  
        String exceptionString = methodName.replace("test", "");  
        Class exceptionClass = Class.forName("java.lang." + exceptionString);  
        Exception expected = (Exception) exceptionClass.newInstance();  
        // test some api and assume it return RuntimeException.
        Exception response = new RuntimeException();
        assertEquals(expected.getClass().getName(), response.getClass().getName());  
    }  
}
```

애너테이션은 이 모든 문제를 해결해주는 개념이다.

<br>

## 애너테이션 (Annotation)
JUnit도 버전 4부터 애너테이션을 전면 도입했다. 지금부터 나오는 샘플 코드에서는 애너테이션의 동작 방식을 보여주고자 직접 제작한 작은 테스트 프레임워크를 사용한다.
```java
// 직접 구현한 애너테이션으로, 테스트 메서드임을 선언하는 역할을 한다. 매개변수 없는 정적 메서드 전용이다.  
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
}
```  
- 애너테이션 선언에 있는 다른 애너테이션들을 메타 애너테이션 (meta-annotation)이라고 한다.  
  - @Retention: @Test가 런타임에도 유지되어야 한다는 표시다. 만약 이를 생략하면 텐스트 도구는 @Test를 인식할 수 없다.  
  - @Target: 반드시 메서드 선언에서만 사용돼야 한다는 표시다. 따라서 클래스 선언, 필드 선언 등 다른 프로그램 요소에는 달 수 없다.
- 매개변수가 있거나 인스턴스 메서드에 @Test를 선언한다면 컴파일은 잘 되겠지만, 테스트 도구를 실행할 때 문제가 된다.
  - 인스턴스가 없기 때문
```java
public class TestSample {  
    @Test public static void m1() {}
    public static void m2() {}
    @Test public static void m3() { throw new RuntimeException("failure"); } 
    public static void m4() {}
    @Test public void m5() {}  
    public static void m6() {}  
    @Test public static void m7() { throw new RuntimeException("failure"); }  
    public static void m8() {}  
}
```
@Test 애너테이션이 TestSample 클래스의 의미에 직접적인 영향을 주지는 않는다. 그저 이 애너테이션에 관심 있는 프로그램에게 추가 정보를 제공할 뿐이다. 즉, 대상 코드의 의미는 그대로 둔 채 그 애너테이션에 관심 있는 도구에서 특별한 처리를 할 기회를 준다. 아래 예제를 살펴보도록 하자.
```java
public class RunTest {  
    public static void main(String[] args) throws Exception {  
        int tests = 0;  
        int passed = 0;  
        Class<?> testClass = TestSample.class;  
        for (Method m: testClass.getDeclaredMethods()) {  
            if (m.isAnnotationPresent(Test.class)) {  
                tests++;
                try {  
                    m.invoke(null);  
                    passed++;  
                } catch (InvocationTargetException wrappedExc) {  
                    Throwable exc = wrappedExc.getCause();  
                    System.out.println(m + " 실패: " + exc);  
                } catch (Exception exc) {  
                    System.out.println("잘못 사용한 @Test: " + m);  
                }  
            }  
        }  
        System.out.printf("성공: %d, 실패: %d%n", passed, tests - passed);  
    }  
}
```
RunTest의 결과는 다음과 같다.
```
잘못 사용한 @Test: public void kr._19fstudy.effective_java.ch6.item39.test.TestSample.m5()
public static void kr._19fstudy.effective_java.ch6.item39.test.TestSample.m7() 실패: java.lang.RuntimeException: failure
public static void kr._19fstudy.effective_java.ch6.item39.test.TestSample.m3() 실패: java.lang.RuntimeException: failure
성공: 1, 실패: 3
```

### 특정 예외를 던져야만 성공하는 테스트를 지원하도록 해보자.

```java
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.METHOD)  
public @interface ExceptionTest {  
    Class<? extends Throwable> value();  
}
```
위와 같이 선언하면 Class<? extends Throwable> 타입의 매개변수를 받을 수 있다. 즉, Throwable 을 확장한 클래스의 Class 객체는 모두 받을 수 있다는 것이다.
```java
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
```
결과는 다음과 같다.
```
테스트 public static void kr._19fstudy.effective_java.ch6.item39.exception_test.ExceptionTestSample.m2() 실패: 기대한 예외 java.lang.ArithmeticException, 발생한 예외 java.lang.ArrayIndexOutOfBoundsException: Index 1 out of bounds for length 0
테스트 public static void kr._19fstudy.effective_java.ch6.item39.exception_test.ExceptionTestSample.m3() 실패: 예외를 던지지 않음
성공: 1, 실패: 2
```

### 예외를 여러 개 명시하고 그중 하나가 발생하면 성공하게 만들 수도 있다.
```java
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.METHOD)  
public @interface MultipleExceptionTest {
    // 배열 매개변수를 받는 애너테이션용 문법은 아주 유연하다.
    // 다음과 같이 선언하면, 앞서 선언했던 단일 원소도 모두 수정 없이 수용한다.
    // 원소가 여럿인 배열을 지정할 때는 원소들을 중괄호로 감싸고 쉼표로 구분해주기만 하면 된다.
    Class<? extends Throwable>[] value();
}
```
```java
@MultipleExceptionTest({IndexOutOfBoundsException.class, NullPointerException.class})  
public static void doubleyBad() {  
    List<String> list = new ArrayList<>();  
    // 책에서는 '자바 API 명세에 따르면 다음 메서드는 IndexOutOfBoundsException 이나,  
    // NullPointerException을 던질 수 있다.' 고 하는데 나는 아직 경험해보지 못했다....
    list.addAll(5, null);  
}
```
결과는 다음과 같다.
```
성공: 1, 실패: 0
```

### 자바 8에서는 여러 개의 값을 받는 애너테이션을 다른 방식으로도 만들 수 있다.

@Repeatable 메타 애너테이션
- @Repeatable을 메타 애너테이션으로 사용한 애너테이션은 하나의 프로그램 요소에 여러 번 달 수 있다.
- @Repeatable을 단 애너테이션을 반환하는 ‘컨테이너 애너테이션’을 하나 더 정의하고, @Repeatable에 이 컨테이너 애너테이션의 class 객체를 매개변수로 전달해야 한다.
- 내부 애너테이션 타입의 배열을 반환하는 value 메서드를 정의해야 한다.
- 컨테이너 애너테이션 타입에는 적절한 보존 정책 (@Retention)과 적용 대상(@Target)을 명시해야 한다. 그렇지 않으면 컴파일 에러가 발생한다.
    
```java
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.METHOD)  
@Repeatable(RepeatableExceptionTestContainer.class)  
public @interface RepeatableExceptionTest {  
    Class<? extends Throwable> value();  
}
```
```java
// Container Annotation
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.METHOD)  
public @interface RepeatableExceptionTestContainer {  
    RepeatableExceptionTest[] value();  
}
```
```java
@RepeatableExceptionTest(IndexOutOfBoundsException.class)  
@RepeatableExceptionTest(NullPointerException.class)  
public static void doubleyBad() {  
    List<String> list = new ArrayList<>();  
    list.addAll(5, null);  
}
```
**주의할 점**
- @RepeatableExceptionTest 애너테이션을 1개만 사용했을 때와 여러 개를 사용했을 때 애너테이션 타입이 달라진다.
-   여러 개 달면 하나만 달았을 때와 구분하기 위해 해당 ‘컨테이너 애너테이션 타입’ 이 적용된다.
-   isAnnotationPresent() 는 애너테이션 타입을 명확히 구분하기 때문에 @RepeatableExceptionTest 를 여러 개 단 다음, isAnnotationPresent(RepeatableExceptionTest.class) 를 한다면 ‘false’를 반환하게 된다.
-   그 결과, 애너테이션을 여러 번 단 메서드들을 모두 무시하고 지나친다. 그래서 애너테이션을 처리하는 곳에서는 각각 확인해줘야 한다.
```java
public static void main(String[] args) throws Exception {  
    int tests = 0;  
    int passed = 0;  
    Class<?> testClass = RepeatableExceptionTestSample.class;  
    for (Method m: testClass.getDeclaredMethods()) {  
        if (m.isAnnotationPresent(RepeatableExceptionTest.class)  
        || m.isAnnotationPresent(RepeatableExceptionTestContainer.class)) {  
            tests++;  
            try {  
                m.invoke(null);  
                System.out.printf("테스트 %s 실패: 예외를 던지지 않음%n", m);  
            } catch (Throwable wrappedExc) {  
                Throwable exc = wrappedExc.getCause();  
                int oldPassed = passed;  
                RepeatableExceptionTest[] excTests = m.getAnnotationsByType(RepeatableExceptionTest.class);  
                for (RepeatableExceptionTest excTest: excTests) {  
                    if (excTest.value().isInstance(exc)) {  
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
```

<br>
  
## 결론
- 반복 가능 애너테이션을 사용해 사용하는 측면에서의 코드 가독성을 높였다. 하지만, 처리하는 곳에서는 코드 양이 늘어나고 처리 코드가 복잡해질 수 있다.
- 그럼에도 불구하고 명명패턴보다는 애너테이션이 낫다. 즉, 애너테이션으로 할 수 있는 일을 명명 패턴으로 처리할 이유는 없다.
- 자바 프로그래머라면 자바가 제공하는 애너테이션 타입들을 사용하도록 하자.
- IDE나 정적 분석 도구가 제공하는 애너테이션도 코드 품질을 높여줄 수 있다. 다만, 그 애너테이션들은 표준이 아니니 도구를 바꾸거나 표준이 만들어지면 수정 작업을 조금 거쳐야 할 것이다.

<br><br><br><br>
References
- Effective Java, 3rd Edition, by Joshua Bloch, KOREAN language edition published by INSIGHT PRESS, Copyright 2018.