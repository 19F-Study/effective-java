# Item 47. 반환 타입으로는 스트림보다 컬렉션이 낫다

### 자바 7 & 8
- 원소 시퀀스, 일련의 원소를 반환하는 메서드는 수없이 많다
    - 자바 7까지는 이런 메서드의 반환 타입으로 Collection, Set, List 같은 컬렉션 인터페이스/Iterable/배열을 사용
        - 기본은 컬렉션 인터페이스
        - for-each 문에서만 쓰이거나 반환된 원소 시퀀스가(주로 contains(Object)) 일부 Collection 메서드를 구현할 수 없을 때는 Iterable 인터페이스를 사용
        - 성능에 민감한 경우 배열
    - 자바 8
        - 스트림이라는 개념이 생김 
        - 스트림은 반복을 지원하지 않음   
        - 스트림은 Iterable 인터페이스가 정의한 추상 메서드를 전부 포함할 뿐 아니라, Iterable 인터페이스가 정의한 방식대로 동작  
        하지만, 
        - 스트림은 Iterable을 extend 하지 않았음
        - 따라서 for-each로 반복할 수 없음
                          
- 스트림과 반복을 알맞게 조합해야 좋은 코드가 될 수 있다

### Stream -> Iterable/Iterable -> Stream (어댑터 패턴 사용)

```java
public static <E> Iterable<E> iterableOf(Stream<E> stream) {
    return stream::iterator;
}

public static <E> Stream<E> streamOf(Iterable<E> iterable) {
    return StreamSupport.stream(iterable.spliterator(), false);
}
```

### 객체 시퀀스를 반환하는 메서드를 작성할때
1. 이 메서드가 스트림 파이프라인에서만 쓰일 걸 아는 경우 -> 스트림 반환
2. 반환된 객체들이 반복문에서만 쓰일 걸 아는 경우 -> Iterable반환
3. 공개 API의 경우 -> 스트림 파이프라인과 반복문을 사용하려는 사람 모두를 배려해야함
    - Collection 인터페이스는 Iterable의 하위 타입이고, stream 메서드도 제공하니 반복과 스트림을 동시에 지원한다  
    - **원소 시퀀스를 반환하는 공개 AP의 반환 타입에서는 Collection이나 그 하위 타입을 쓰는게 일반적으로 최선이다** 
        -  ex) Arrays의 경우 Arrays.asList와 Stream.of 메서드 모두 제공
    - 반환하는 시퀀스의 크기가 메모리에 올려도 안전할 만큼 작다면 ArrayList나 HashSet 같은 표준 컬렉션 구현체를 반환하는 것이 최선
    - 하지만 컬렉션을 반환한다는 이유로 덩치 큰 시퀀스를 메모리에 올리면 안됨

### 전용 컬렉션 구현
멱집합(한 집합의 모든 부분 집합을 원소로 하는 집합)  
{a, b, c}의 멱집합은 [{}, {a}, {b}, {c}, {a, b}, {a, c}, {b, c}, {a, b, c}]  
원소의 개수가 n개면 멱집합의 원소의 갯수는 2^n개 (멱집합을 표준 컬렉션 구현체에 저장하려는 생각은 위험함)  
- AbstractList를 이용하여 전용 컬렉션 구현
(AbstractCollection을 활용해 Collection 구현체를 작성할 때는 Iterable용 메서드 외에 contains와 size를 구현,   
만약 이를 구현 불가능한 경우 Iterable을 반환)

### 스트림 반환
-입력 리스트의 모든 부분리스트를 스트림으로 구현하기(예제)
- 반복을 사용하는게 더 자연스러운 상황에서도 사용자는 스트림을 쓰거나 Stream을 Iterable로 변환해주는 어댑터를 이용해야 함  
    - 어댑터를 클라이언트 코드를 어수선하게 만듦
    - 직접 구현한 전용 Collection을 사용하는 것이 스트림을 활용한 구현보다 더 빠름

### 정리
- 스트림으로의 처리, 반복으로의 처리를 원하는 사용자가 모두 있을 수 있음을 떠올리며 양쪽을 만족시키기 위해 노력해야함
- 컬렉션을 반환할 수 있는 경우 컬렉션을 반환하자
    - 원소의 수가 적다면 표준 컬렉션을 반환
    - 아닌 경우 전용 컬렉션 구현을 고민해아야 함
- 컬렉션 반환이 불가능하면 스트림과 Iterable 중 더 자연스러운 것을 반환