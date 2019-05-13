# Item 77, 예외를 무시하지 말라

## 참고 및 출처

- Joshua Bloch. (2018). 이펙티브 자바 3판 (개앞매시(이복연) 옮김). 인사이트

## 예외를 무시하는 흔한 예
```java
    try {
        ...
    } catch (SomeException e) {
        // do nothing
    }
```

- 예외는 문제 상황에 잘 대처하기 위해 존재하는데 catch 블록을 비워두면 예외가 존재할 이유가 없어진다.

## 예외를 무시할 때

- 물론 예외를 무시해야 할 때도 있다. FileInputStream을 닫을 때가 그렇다. 입력 전용 스트림이므로 파일의 상태를 변경하지 않았으니 복구할 것이 없으며, 스트림을 닫는다는 건 필요한 정보는 이미 다 읽었다는 뜻이니 남은 작업을 중단할 이유도 없다. 그러나 로그 정도는 남기는 것이 좋다.
- 예외를 무시하기로 했다면 catch 블록 안에 그렇게 결정한 이유를 주석으로 남기고 예외 변수의 이름도 ignored로 바꿔놓도록 하자.
```java
    Future<Integer> future = exec.submit(planarMap::chromaticNumber);
    
    int numColors = 4;
    try {
        numColors = f.get(1L, TimeUnit.Seconds);
    } catch (TimeoutException | ExecutionException ignored) {
        // 기본 값을 사용한다(색상 수를 최소화하면 좋지만, 필수는 아니다).
    }
```