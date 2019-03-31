# Item 37, ordinal 인덱싱 대신 EnumMap을 사용하라

## 참고 및 출처

- Joshua Bloch. (2018). 이펙티브 자바 3판 (개앞매시(이복연) 옮김). 인사이트
- [https://docs.oracle.com/javase/tutorial/extra/generics/methods.html](https://docs.oracle.com/javase/tutorial/extra/generics/methods.html)

## Ordinal Indexing

- 아이템 37은 아이템 35의 특수 사례이다.
- Ordinal Indexing 이란?
    - Ordinal Indexing이란 Enum의 각 원소를 key로 하고 value로 collection을 갖도록 하는 자료 구조를 표현하기 위한 하나의 방법이다. 책에서 나온 예제는 Enum의 원소의 개수 만큼을 길이로 하는 배열을 만들고 각 Enum의 ordinal 값을 그 배열의 인덱스로 사용하는 방법이다. 그러나 이런 방법은 문제가 많다.
- Ordinal Indexing의 문제
    - 배열은 제네릭과 호환되지 않으니 비검사 형변환을 수행해야 하고 깔끔히 컴파일되지 않는다.
    - 배열은 각 인덱스의 의미를 모르니 출력 결과에 직접 레이블을 달아야 한다.
    - 정확한 정숫값을 사용한다는 것이 코드를 작성한 사람이 직접 보증해야 한다. 정수는 열거 타입과 달리 타입 안전하지 않기 때문이다.

## EnumMap

- [https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/EnumMap.html](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/EnumMap.html#%3Cinit%3E(java.lang.Class))
    - null 값은 키로 허용되지 않는다. 그러나 null value는 허용된다.
    - 쓰레드 안전하지 않다.
    - 모든 기본 operation들은 constant time의 실행 시간을 보장하고 HashMap 보다 대부분의 operation 들은 HashMap에 비해 빠른 편이다.
- EnumMap은 Ordinal Indexing을 사용하지 않고 위에서 표현하고 싶었던 자료 구조를 표현할 수 있는 방법이다. 더 짧고 명료하고 안전하며 성능도 원래 버전과 비등하다. 안전하지 않은 형변환은 쓰지 않고, 맵의 키인 열거 타입이 그 자체로 출력용 문자열도 제공한다. EnumMap의 내부 구현이 배열을 사용하기 때문에 ordinal을 쓴 배열에 비교해도 성능도 비등하다.
- EnumMapExample2 예제
    - 이 예제에서는 EnumMap이 일반 맵 구현체보다 공간과 성능에서 이점이 있기 때문에 stream.collect(groupingBy()) 를 이용할 때 EnumMap를 이용해 맵 구현체로 EnumMap을 이용할 수 있게 하는 방법을 보여준다.