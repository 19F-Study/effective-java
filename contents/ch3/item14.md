# Item 14, Comparable을 구현할지 고려하라

## 참고 및 출처

- [https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Comparable.html](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Comparable.html)
- Joshua Bloch. (2018). 이펙티브 자바 3판 (개앞매시(이복연) 옮김). 인사이트

### Comparable
```java
    public interface Comparable<T> {
        int compareTo(T t);
    }
```

- Comparable는 compareTo 메서드를 가지는 인터페이스이다.
- Object의 equals와 다른 점
    - compareTo는 단순 동치성 비교에 더해 순서까지 비교할 수 있다.
    - 제네릭하다.
- Comparable을 구현했다는 것은 그 클래스의 인스턴스 그 클래스의 인스턴스들에는 자연스러운 순서(natural order)가 있음을 뜻한다. Comparable 인터페이스 구현을 통해 정렬, 검색, 극단값 계산, 자동 정렬되는 컬렉션 관리 등을 쉽게 할 수 있다.
- 알파벳, 숫자, 연대 같이 순서가 명확한 값 클래스를 작성한다면 반드시 Comparable 인터페이스를 구현하자.

### compareTo 메서드의 일반 규약

- 이 객체와 주어진 객체의 순서를 비교한다. 이 객체가 주어진 객체보다 작으면 음의 정수를, 같으면 0을, 크면 양의 정수를 반환한다. 이 객체와 비할 수 없는 타입의 객체가 주어지면 ClassCastException을 던진다.
- 다음 설명에서 sgn(표현식) 표기는 수학에서 말하는 부호 함수(signum function)를 뜻하며, 표현식의 값이 음수, 0, 양수일 때 -1, 0, 1을 반환하도록 정의했다.
    - Comparable을 구현한 클래스는 모든 x, y에 대해 sgn(x.compareTo(y)) == -sgn(y.compareTo(x))여야 한다(따라서 x.compareTo(y)는 y.compareTo(x)가 예외를 던질 때에 한해 예외를 던져야 한다).
        - *두 객체 참조의 순서를 바꿔 비교해도 예상한 결과가 나와야 한다는 얘기다.*
        - *첫 번째 객체가 두 번째 객체보다 작으면, 두 번째 객체가 첫 번째 객체보다 커야한다. 첫 번째가 두 번째와 크기가 같다면, 두 번째는 첫 번째와 같아야 한다. 마지막으로 첫 번째가 두 번째보다 크면, 두 번째는 첫 번째보다 작아야 한다.*
    - Comparable을 구현한 클래스는 추이성을 보장해야 한다. 즉, (x.compareTo(y) > 0 && y.compareTo(z) > 0)이면 x.compareTo(z) > 0 이다.
        - *첫 번째가 두 번째보다 크고 두 번째가 세 번째보다 크면, 첫 번째는 세 번째보다 커야 한다는 뜻이다.*
    - Comparable을 구현한 클래스는 모든 z에 대해 x.compareTo(y) == 0 이면 sgn(x.compareTo(z)) == sgn(y.compareTo(z))다.
        - *크기가 같은 객체들끼리는 어떤 객체와 비교하더라도 항상 같아야 한다는 뜻이다.*
    - 이번 권고는 필수는 아니지만 꼭 지키는게 좋다. (x.compareTo(y) == 0) == (x.equalsy))여야 한다. Comparable을 구현하고 이 권고를 지키지 않는 모든 클래스는 그 사실을 명시해야 한다. 다음과 같이 명시하면 적당할 것이다.

    *"주의: 이 클래스의 순서는 equals 메서드와 일관되지 않다."*

### 규약과 관련해 주의할 사항

- hashCode 규약을 지키지 못하면 해시를 사용하는 클래스와 어울리지 못하듯, compareTo 규약을 지키지 못하면 비교를 활용하는 클래스와 어울리지 못한다. 예를 들어 TreeSet과 TreeMap, 검색과 정렬 알고리즘을 활용하는 유틸리티 클래스인 Collections와 Arrays가 있다.
- compareTo 메서드의 일반 규약을 읽어보면 알겠지만 compareTo 메서드로 수행하는 동치성 검사도 equals 규약과 똑같이 반사성, 대칭성, 추이성을 충족해야 함을 뜻한다. 그래서 주의 사항도 똑같다.
    - equals에서와 마찬가지로 기존 클래스를 확장한 구체 클래스에서 새로운 값 컴포넌트를 추가했다면 compareTo 규약을 지킬 방법이 없다. 우회 방법 또한 같다. 확장하는 대신 구성하는 것이다.
- 마지막 규약은 잘 지키길 권유한다. compareTo 메서드로 수행한 동치성 테스트의 결과가 equals와 같아야 한다는 것이다. 반드시 지키지 않아도 동작은 하나 정렬된 컬렉션을 사용할 때 equals와 엇박자를 낼 수 있다.

### 구현에 있어서 equals와의 차이점

- 모든 객체에 대해 전역 동치관계를 부여하는 equals 메서드와 달리 compareTo는 타입이 다른 객체를 신경쓰지 않아도 된다. 타입이 다른 객체가 주어지면 간단히 ClassCastException을 던져도 되며, 대부분 그렇게 한다.
- Comarable은 타입을 인수로 받는 제네릭 인터페이스이므로 compareTo 메서드의 인수 타입은 컴파일 타임에 정해진다. 입력 인수의 타입을 확인하거나 형 변환할 필요가 없다는 뜻이다. 인수의 타입이 잘못됐다면 컴파일 자체가 되지 않는다.

### compareTo 작성 요령

- compareTo 메서드의 인수 타입은 컴파일 타입에 정해진다. 입력 인수의 타입을 확인하거나 형변환할 필요가 없다.
- null을 인수로 넣어 호출하면 NullPointerException을 던져야 한다.
- compareTo 메서드는 각 필드가 동치인지를 비교하는 것이 아니라 순서를 비교하는 것이다. 객체 참조 필드를 비교하려면 compareTo 메서드를 재귀적으로 호출한다. 참조 타입을 비교하는데, Comparable을 구현하지 않은 필드나 표준이 아닌 순서로 비교해야 한다면 비교자(Comparator)를 대신 사용한다.
- 정수 및 실수 기본 타입을 비교할 때는 박싱된 기본 타입 클래스에 새로 추가된 정적 메서드인 compare를 이용하면 된다. 관계 연산자 '<'와 '>'를 사용하는 방식은 거추장스럽고 오류를 유발해 추천하지 않는다.
- 클래스에 핵심 필드가 여러 개라면 어느 것을 먼저 비교하느냐가 중요해진다. 가장 핵심적인 필드부터 비교해나가자. 비교 결과가 0이 아니면 즉시 반환하고 0이라면 다음으로 중요한 필드를 비교해 나간다.
- Comparator 인터페이스가 일련의 비교자 생성 메서드와 함께 메소드 연쇄 방식으로 비교자를 생성할 수 있게 되었다. 이 방식은 간결함이 장점이지만 성능 저하가 있을 수 있기 때문에 주의해야 한다.
- '값의 차'를 기준으로 첫 번째 값이 두 번째 값보다 작으면 음수를, 두 값이 같으면 0을, 첫 번째 값이 크면 양수를 반환하는 구현을 마주할 수 있다. 이 방식은 사용하면 안된다. 이 방식은 정수 오버플로를 일으키거나 부동소수점 계산 방식에 따른 오류를 낼 수 있다.