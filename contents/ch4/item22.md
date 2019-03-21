# Item 22, 인터페이스는 타입을 정의하는 용도로만 사용하라

## 참고 및 출처

- Joshua Bloch. (2018). 이펙티브 자바 3판 (개앞매시(이복연) 옮김). 인사이트

### 인터페이스의 의미

- 인터페이스는 자신을 구현한 클래스의 인스턴스를 참조할 수 있는 타입 역할을 한다. 달리 말해, 클래스가 어떤 인터페이스를 구현한다는 것은 자신의 인스턴스로 무엇을 할 수 있는지를 클라이언트에 얘기해주는 것이다.

### 인터페이스 안티 패턴 - 상수 인터페이스
```java
    package java.io;
    
    /**
     * Constants written into the Object Serialization Stream.
     *
     * @author  unascribed
     * @since 1.1
     */
    public interface ObjectStreamConstants {
    
        /**
         * Magic number that is written to the stream header.
         */
        static final short STREAM_MAGIC = (short)0xaced;
    
        /**
         * Version number that is written to the stream header.
         */
        static final short STREAM_VERSION = 5;
    
    ... 
```
- 클래스 내부에서 사용하는 상수는 외부 인터페이스가 아니라 내부 구현에 해당한다. 따라서 상수 인터페이스를 구현하는 것은 이 내부 구현을 클래스의 API로 노출하는 행위다. 이는 사용자들에게 혼란을 줄 수 있고 클라이언트 코드가 이 상수에 종속되게 할 수 있기 때문에 안티패턴이다.
- 상수를 공개할 목적이라면 더 합당한 선택지가 몇 가지 있다.
    - 특정 클래스나 인터페이스와 강하게 연관된 상수라면 그 클래스나 인터페이스 자체에 추가해야 한다.
    - 열거 타입으로 나타내기 적합한 상수라면 열거 타입으로 만들어 공개하면 된다.
    - 그것도 아니라면, 인스턴스화 할 수 없는 유틸리티 클래스에 담아 공개하자.

```java
        package java.lang;
        
        public final class Integer extends Number implements Comparable<Integer> {
            /**
             * A constant holding the minimum value an {@code int} can
             * have, -2<sup>31</sup>.
             */
            @Native public static final int   MIN_VALUE = 0x80000000;
        
            /**
             * A constant holding the maximum value an {@code int} can
             * have, 2<sup>31</sup>-1.
             */
            @Native public static final int   MAX_VALUE = 0x7fffffff;
```

