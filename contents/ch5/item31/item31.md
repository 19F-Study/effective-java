# Item 31, 한정적 와일드카드를 사용해 API 유연성을 높이라

## 참고 및 출처

- Joshua Bloch. (2018). 이펙티브 자바 3판 (개앞매시(이복연) 옮김). 인사이트
- [https://docs.oracle.com/javase/tutorial/extra/generics/methods.html](https://docs.oracle.com/javase/tutorial/extra/generics/methods.html)

### 제네릭만 사용했을 때 오는 결함

- 매개변수화 타입은 불공변(invariant)이다. 즉, 서로 다른 타입 Type1과 Type2가 있을 때 List<Type1>은 List<Type2>의 하위 타입도 상위 타입도 아니다.
    - ex) List<String>은 List<Object>의 하위 타입이 아니라는 말이다. List<String>은 List<Object>가 하는 일을 제대로 수행하지 못하니 하위 타입이 될 수 없다.
- 이럴 때 어떤 불편함이 올 수 있을까?
```Java
    public class Stack<E> {
        public Stack();
        public void push(E e);
        public E pop();
        public boolean isEmpty();
        public void pushAll(Iterable<E> src);
            public void popAll(Collection<E> src);
    }
```
- 위와 같은 Stack이 있을 때 Stack<Number>로 선언한다면 pushAll에 Iterable<Integer> 타입의 변수를 대입했을 때 제대로 동작하지 않는다.
- 논리적으로는 잘 동작해야 할 것 같다. 그러나 매개변수화 타입이 불공변(invariant)이기 때문에 자바에서는 잘 동작하지 않는다. 논리적인 동작과 실제 동작을 맞춰주려면 pushAll의 입력 매개변수 타입은 'E의 Iterable'이 아니라 'E의 하위 타입의 Iterable'임을 알려 주어야 한다. Iterable<? extends E> 가 정확히 이런 뜻이다.
- popAll에서도 비슷한 오류가 발생한다. 이번에는 popAll의 매개변수 타입이 'E의 Collection' 이 아니라 'E의 상위 타입의 Collection'이어야 한다. 와일드카드 타입을 사용한 Collection<? super E>가 정확히 이런 의미다.
- 유연성을 극대화하려면 원소의 생산자나 소비자용 입력 매개변수에 와일드카드 타입을 사용하라
    - 생산자 <? extends E>
    - 소비자 <? super E>
    - 입력 매개변수가 생산자와 소비자 역할을 동시에 한다면 와일드카드 타입을 써도 좋을 게 없다. 오히려 이 때는 타입을 정확히 지정해야 하는 상황으로, 이때는 와일드카드 타입을 쓰지 말아야 한다.
- 펙스(PECS): producer-extends, consumer-super
    - 즉, 매개변수화 타입 T가 생산자라면 <? extends T>를 사용하고, 소비자라면 <? super T>를 사용하라.
        - Stack 예에서 pushAll의 src 매개변수는 Stack이 사용할 E 인스턴스를 생산하므로 src의 적절한 타입은 Iterable<? extends E>이다. 한편, pushAll의 dst 매개변수는 Stack으로부터 E 인스턴스를 소비하므로 dst의 적절한 타입은 Collection<? super E>이다.
    - PECS 공식은 와일드카드 타입을 사용하는 기본 원칙이다.
- 반환 타입에는 한정적 와일드카드 타입을 사용하면 안 된다. 유연성을 높여주기는 커녕 클라이언트 코드에서도 와일드카드 타입을 써야하기 때문이다.
- 클래스 사용자가 와일드카드 타입을 신경써야 한다면 그 API에 무슨 문제가 있을 가능성이 크다.
- 메서드를 정의할 때 타입 매개변수와 와일드카드 중 어떤게 더 나을까?

### 언제 제네릭 메서드를 쓰고 언제 와일드카드 타입을 써야하나?

- 메서드 선언에 타입 매개변수가 한 번만 나오면 와일드카드로 대체하라. 이 때 비한정적 타입 매개변수라면 비한정적 와일드카드로 바꾸고, 한정적 타입 매개변수라면 한정적 와일드카드로 바꾸면 된다.

```Java
    // ref: https://docs.oracle.com/javase/tutorial/extra/generics/methods.html
    interface Collection<E> {
        public boolean containsAll(Collection<?> c);
        public boolean addAll(Collection<? extends E> c);
    }
    
    //
    
    interface Collection<E> {
        public <T> boolean containsAll(Collection<T> c);
        public <T extends E> boolean addAll(Collection<T> c);
        // Hey, type variables can have bounds too!
    }
```
- 타입 퍼러미터 T가 한 번 밖에 쓰이지 않았다.  리턴 타입이 타입 퍼러미터에 의존적이지 않고 다른 어떤 메소드의 인자 또한 타입 퍼러미터에 의존적이지 않다. 이 말은 타입 퍼러미터가 단순히 다형성을 위해 쓰여졌다는 말이다(그리고 이것의 유일한 효과는 다양한 실제 인수 유형을 여러 곳에서 사용할 수 있게 하는 것이다). 그렇다면 와일드카드를 써야 한다. 와일드카드는 유연한 서브타이핑을 지원하도록 설계되었다.
- 제네릭 메서드를 사용하면 타입 퍼러미터가 하나 이상의 아규먼트나 리턴 타입 간의 디펜던시를 나타내기 위해 사용될 수 있다. 제네릭 메서드는 이런 디펜던시가 없다면 사용되어서는 안 된다.
```Java
    // ref: https://docs.oracle.com/javase/tutorial/extra/generics/methods.html
    class Collections {
        public static <T> void copy(List<T> dest, List<? extends T> src) {
        ...
    }
    
    class Collections {
        public static <T, S extends T> void copy(List<T> dest, List<S> src) {
        ...
    }
```

- 먼저 위의 예에서 디펜던시를 살펴보자. src List에 있는 인스턴스들은 dest 리스트의 타입인 T에 대입 가능하여야 한다. 이 때 적합한 표현은 "src의 element들의 타입이 무엇이건 T의 subtype이어야 한다" 이다.
- 첫 번째 copy에서는 이를 타입 퍼러미터 T와 와일드카드를 이용해 나타내었다. 깔끔하다.
- 구현을 두 번째 메서드처럼 할 수도 있다. 그러나 자세히 보자. 타입 퍼러미터를 장의한 부분을 보자. 첫 번째 퍼러미터 T가 있고 두 번째 타입 퍼러미터 S가 있다. T는 dest의 타입과 S를 나타내기 위해 두 번 사용되는 동안 S는 단지 src의 타입을 나타내기 위해 한 번만 사용되었다. 그리고 이 것에 디펜던시를 가지는 것은 아무것도 없다. 이것이 S를 와일드카드로 바꿀 수 있다는 신호다. 와일드카드를 사용하는 것이 더 깔끔하고 간결한 방법이기 때문에 더 선호되어야 한다.