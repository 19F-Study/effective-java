# Item 38, 확장할 수 있는 열거 타입이 필요하면 인터페이스를 사용하라

## 참고 및 출처

- Joshua Bloch. (2018). 이펙티브 자바 3판 (개앞매시(이복연) 옮김). 인사이트
- [https://docs.oracle.com/javase/tutorial/extra/generics/methods.html](https://docs.oracle.com/javase/tutorial/extra/generics/methods.html)

## Enum vs Typesafe enum pattern

- Typesafe enum pattern?
    - [https://stackoverflow.com/questions/5092015/advantages-of-javas-enum-over-the-old-typesafe-enum-pattern](https://stackoverflow.com/questions/5092015/advantages-of-javas-enum-over-the-old-typesafe-enum-pattern)
- 열거 타입은 거의 모든 상황에서 타입 안전 열거 패턴보다 우수하다. 단, 예외가 하나 있으니, 타입 안전 열거 패턴은 확장할 수 있으나 열거 타입은 그럴 수 없다는 점이다.
    - 이는 열거 타입에서 열거한 값들을 그대로 가져온 다음 값을 추가하여 다른 목적으로 쓸 수 없다는 말이다.
    - 사실 대부분의 경우에 열거 타입을 확장하는 것은 좋지 않은 생각이다. 확장한 타입의 원소는 기반 타입의 원소로 취급하지만 그 반대는 성립하지 않는다.
    - 기반 타입과 확장된 타입들의 원소 모두를 순회할 방법도 마땅치 않다.
    - 확장성을 높이려면 고려할 요소가 늘어나 설계와 구현이 복잡해진다.
- 확장할 수 있는 열거 타입이 어울리는 예
    - 연산 코드(operation code, opcode)와 같이 확장할 수 있는 열거 타입이 어울리는 쓰임이 있다. 이따금 API가 제공하는 기본 연산 외에 사용자 확장 연산을 추가할 수 있도록 열어줘야 할 때가 있다.
    - 인터페이스를 먼저 정의하고 열거 타입이 그 인터페이스를 구현하게 하면 된다.
- 열거 타입이 확장할 수 없다는 점은 인터페이스를 구현하게 하는 것을 통해 어느정도 원하는 기능을 구현할 수 있다. 그러나 이 방법에도 문제가 있다. 열거 타입끼리 구현을 상속할 수 없다는 점이다.
    - 아무 상태에도 의존하지 않으면 디폴트 구현을 통해 인터페이스에 추가하는 방법이 있다.
    - java.nio.LinkOption