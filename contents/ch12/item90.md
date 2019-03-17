# item90) 직렬화된 인스턴스 대신 직렬화 프록시 사용을 검토하라
Serializable을 구현하는 순간 언어의 정상적인 매커니즘인 생성자 이외의 방법으로 인스턴스를 생성할 수 있게된다.    
이로 인해 버그와 보안 문제가 일어날 가능성이 커지지만,  
직력화 프록시 패턴 (serialization proxy pattern)을 사용하면 위험을 줄일 수 있다.

## 직렬화 프록시 패턴 작성 방법  
1. 바깥 클래스의 논리적 상태를 정밀하게 표현하는 중첩 클래스를 설계해 private static으로 선언한다.  
2. 중첩 클래스의 생성자는 바깥 클래스를 매개변수로 받는 단 하나여야 한다.  
3. 중첩 클래스의 생성자는 인수로 넘어온 인스턴스의 데이터를 단순히 복사한다. (일관성 검사나 방어적 복사를 할 필요가 없다.)
4. 바깥 클래스와 중첩 클래스 모두 Serializable을 구현한다.  
5. 바깥 클래스에 writeReplace 메서드를 추가한다.
6. 바깥 클래스와 논리적으로 동일한 인스턴스를 반환하는 readResolve 메서드를 중첩 클래스에 추가한다.

-> 중첨 클래스가 직렬화 프록시이며, 직렬화 프록시의 기본 직렬화 형태는 바깥 클래스의 직렬화 형태로 쓰기에 이상적이다.  
writeReplace는 자바의 직렬화 시스템이 바깥 클래스의 인스턴스 대신 프록시 인스턴스를 반환하게 하는 역할을 한다.  
즉, 직렬화가 이뤄지기 전에 바깥 클래스의 인스턴스를 직렬화 프록시로 변환해준다.  
readObject 메서드를 바깥 클래스에 추가하면, 불변식을 훼손하고자 하는 시도를 막을 수 있다.  
readResolve는 역직렬화 시에 직렬화 시스템이 직렬화 프록시를 다시 바깥 클래스의 인스턴스로 반환하게 해준다. 

* 장점  
1. 일반 인스턴스를 만들 때와 똑같은 생성자, 정적 팩터리, 혹은 다른 메서드를 사용해 역직렬화된 인스턴스를 생성한다.  
그러므로 클래스의 정적 팩터리나 생성자가 불변식을 확인해주고 인스턴스 메서드들이 불변식을 잘 지켜준다면  
역직렬화된 인스턴스가 해당 클래스의 불변식을 만족하는지 검사할 필요없다.  
2. 가짜 바이트 스트림 공격과 내부 필드 탈취 공격을 프록시 수준에서 차단해준다.  
3. 바깥 클래스의 필드를 final로 선언해도 되므로 진정한 불변으로 만들 수 있다. 
4. 역직렬화한 인스턴스와 원래의 직렬화된 인스턴스의 클래스가 달라도 정상 작동한다. 

EnumSet의 직렬화 프록시
```java
private static class SerializationProxy<E extends Enum<E>>
        implements java.io.Serializable
    {

        private static final Enum<?>[] ZERO_LENGTH_ENUM_ARRAY = new Enum<?>[0];

        /**
         * The element type of this enum set.
         *
         * @serial
         */
        private final Class<E> elementType;

        /**
         * The elements contained in this enum set.
         *
         * @serial
         */
        private final Enum<?>[] elements;

        SerializationProxy(EnumSet<E> set) {
            elementType = set.elementType;
            elements = set.toArray(ZERO_LENGTH_ENUM_ARRAY);
        }

        /**
         * Returns an {@code EnumSet} object with initial state
         * held by this proxy.
         *
         * @return a {@code EnumSet} object with initial state
         * held by this proxy
         */
        @SuppressWarnings("unchecked")
        private Object readResolve() {
            // instead of cast to E, we should perhaps use elementType.cast()
            // to avoid injection of forged stream, but it will slow the
            // implementation
            EnumSet<E> result = EnumSet.noneOf(elementType);
            for (Enum<?> e : elements)
                result.add((E)e);
            return result;
        }

        private static final long serialVersionUID = 362491234563181265L;
    }
```

* 단점
1. 클라이언트가 멋대로 확장할 수 있는 클래스에는 적용할 수 없다.  
2. 객체 그래프에 순환이 있는 클래스에 적용 할 수 없다.  

-> 이런 객체의 메서드를 직렬화 프록시의 readResolve 안에서 호출하려 하면, ClassCastException이 발생한다.  
직렬화 프록시만 가졌을 뿐 실제 객체는 아직 만들어진 것이 아니기 때문이다.

> 출처  
Effective java 3/E (조슈아 블로크)