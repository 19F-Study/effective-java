# Item 55. 옵셔널 반환은 신중히 하라 

### 자바 8 이전, 메서드가 특정 조건에서 값을 반환할 수 없을 때 취할 수 있는 선택지
- 예외던지기 
    - 예외는 진짜 예외적인 상황에서 사용해야 함
    - 예외를 생성할 때 스택 추적 전체를 캡처하므로 비용 문제
- null 반환하기
    - 별도의 null 처리 코드를 추가해야 함(null을 반환하게 한 실제 원인과는 전혀 상관없는 코드에서 NPE가 발생할 수 있음)

### 자바 8로 올라오면서 Optional<T> 선택지가 추가
- Optional<T>는 null이 아닌 T 타입 참조를 하나 담거나, 아무것도 담지 않을 수 있음

## Optional
- 옵셔널은 원소를 최대 1개 가질 수 있는 불변 컬렉션
(Optional<T>가 Collection<T>를 구현하지는 않았지만 원칙적으로)
- 보통은 T를 반환해야 하지만 특정 조건에서는 아무것도 반환하지 않아야 할 때 T 대신 Optional<T>를 반환하도록 선언
    - 예외를 던지는 메서드보다 유연하고 사용하기 쉬우며, null을 반환하는 메서드보다 오류 가능성이 작다.
- 옵셔널을 반환하도록 구현
    - 적절한 정적 팩터리를 사용해 옵셔널을 생성
        - Optional.empty() : 빈 경우
        - Optional.of(value) : 값이 있는 경우
        - Optional.ofNullable(value) : null 값도 허용하느 옵셔널을 만드는 경우 
        **Optional.of(null)을 넣으면 NPE가 발생하므로 주의 해야함**               
    - 옵셔널을 반환하는 메서에서는 절대 null을 반환하지 않아야 한다
- 스트림의 종단 연산 중 상당수가 옵셔널을 반환 

## 옵셔널반환을 선택해야 하는 기준 
- 옵셔널은 검사 예외와 취지가 비슷하다.
- 반환값이 없을 수도 있음을 API 사용자에게 명확히 알려준다
(비검사 예외를 던지거나, null을 반환하는 경우 API 사용자가 이를 인지하지 못하는 경우 발생)

## 옵셔널 활용
1. 기본값을 정해둘 수 있다.
```java
String lastWordInLexicon = max(words).orElse("단어 없음...");
```
2. 원하는 예외를 던질 수 있다.
```java
Toy myToy = max(toys).orElseThrow(TemperTantrumException::new);
```
3. 항상 값이 채워져 있다고 가정
```java
Element lastNobleGas = max(Elements.NOBLE_GASES).get();
```
4. 기본값을 설정하는 비용이 커서 부담이 될 때 Supplier<T>를 인수로 받는 orElseGet을 사용하면, 값이 처음 필요할 때 Supplier<T>를
사용해 생성하므로 초기 설정 비용을 낮출 수 있음 
5. filter, map, flatMap, ifPresent 등과 같이 특별한 쓰임에 대비한 메서드도 준비되어 있음
6. isPresent : 옵셔널이 채워져 있으면 true를, 비어 있으면 false를 반환
```java
Optional<ProcessHandle> parentProcess = ph.parent();
        System.out.println("부모 PID: " + (parentProcess.isPresent() ?
                String.valueOf(parentProcess.get().pid()) : "N/A"));
```

```java
System.out.println("부모 PID: " +
            ph.parent().map(h -> String.valueOf(h.pid())).orElse("N/A"));
```

```java
streamOfOptionals
    .filter(Optional::isPresent)
    .map(Optiona::get)
```
#### 자바 9  Optional
- 옵셔널에 stream() 메서드가 추가되었다.
- 해당 메서드는 Optional을 Stream으로 변환해주는 어댑터 

```java
streamOfOptionals.flatMap(Optional::stream)
```

## 반환값으로의 옵셔널
- 반환값으로 옵셔널을 사용한다고 해서 무조건 득이 되지 않음
- 컬렉션, 스트림, 배열, 옵셔널 같은 컨테이너 타입은 옵셔널로 감싸면 안됨
    - 빈 Optional<List<T>>를 반환하기 보다는 빈 List<T>를 반환하는게 더 좋음
    - 빈 컨테이너를 반환하면 옵셔널 처리 코드를 넣지 않아도 됨
- 결과가 없을 수 있으며, 클라이언트가 이 상황을 특별하게 처리해야 하는 경우 Optional<T>를 반환
- Optional도 엄연히 새로 할당하고 초기화해야 하는 객체이고, 그 안에서 값을 꺼내려면 메서드를 호출해야 하기 때문에 한 단계를 더 거치는 것
    - 따라서 성능이 중요한 경우 옵셔널이 적합하지 않을 수 있음
- 박싱된 기본 타입을 담는 옵셔널은 기본 타입 자체보다 무거울 수 밖에 없음
    - OptionalInt, OptionalLong, OptionalDouble과 같은 int, long, double 전용 옵셔널 클래스가 있음
- 박싱된 기본 타입을 담는 옵셔널을 반환하지 말자
    - 덜 중요한 기본 타입용인 Boolean, Byte, Character, Short, Float는 예외
- 옵셔널을 반환값 이외의 용도로 쓰는 경우는 매우 드물다

## 키로 사용하는 옵셔널
- 옵셔널을 맵 값의 값으로 사용하면 절대 안된다.
    - 키 자체가 없는 경우, 키는 있지만 키가 속이 빈 옵셔널인 경우와 같이 복잡성만 높여서 혼란과 오류 가능성을 높임
- 옵셔널을 컬렉션의 키, 값, 원소나 배열의 원소로 사용하는 게 적절한 상황은 거의 없음

