# Item73. 추상화 수준에 맞는 예외를 던져라


## 저수준 예외를 처리하지 않고, 바깥으로 전파해버린다면?
* 프로그래머를 당황시킨다.
* 내부 구현 방식을 드러내어 윗 레벨 API를 오염시킨다.
* 다음 릴리즈에서 구현 방식을 바꾸면 다른 예외가 튀어나와 기존 클라이언트 프로그램을 깨지게 할 수도 있다.

이 문제를 피하려면, 상위 계층에서는 저수준 예외를 잡아 자신의 추상화 수준에 맞는 예외로 바꿔 던져야 한다. 이를 예외 번역 (Exception Translation) 이라고 한다.

<br>

## 예외 번역 (Exception Translation)
```java
try {
  .. // 저수준 추상화를 이용한다.
} catch (LowerLevelException e) {
  // 추상화 수준에 맞게 번역한다.
  throw new HigherLevelException(…);
}
```

<br>

## 예외 연쇄 (Exception Chaining)
* 예외를 번역할 때 저수준 예외가 디버깅에 도움이 된다면, 예외 연쇄(exception chaining)를 사용하는게 좋다.
* 예외 연쇄란 문제의 근본 원인(cause)인 저수준 예외를 고수준 예외에 실어 보내는 방식이다.
* 그러면 별도의 접근자 메서드 (Throwable의 getCause()) 를 통해 필요하면 언제든 저수준 예외를 꺼내볼 수 있다.

```java
try {
  … // 저수준 추상화를 이용한다.
} catch (LowerLevelException cause){
  // 저수준 예외를 고수준 예외에 실어보낸다.
  throw new HigherLevelException(cause);
}

…

// 이때, 예외 연쇄용으로 설계된 고수준 예외의 생성자는 상위 클래스의 생성자에 이 이 ‘cause’ 를 건네주어 최종적으로 Throwable 생성자까지 건네지게 한다.
class HigherLevelException extends Exception {
  HigherLevelException(Throwable cause) {
    super(cause);
  }
}

```

무턱대고 예외를 전파하는 것보다 예외 번역이 우수한 방법이긴 하지만, 그렇다고 남용하는건 곤란한다. 가급적 저수준 메서드가 반드시 성공하도록 하여 아래 계층에서는 예외가 발생하지 않도록 하는 것이 최선이다.

<br>

## 핵심 정리
* 아래 계층의 예외를 예방하거나 스스로 처리할 수 없고, 그 예외를 상위 계층에 그대로 노출하기 곤란하다면 예외 번역을 사용하라.
* 이때 예외 연쇄를 이용하면 상위 계층에는 맥락에 어울리는 고수준 예외를 던지면서 근본 원인도 함께 알려주어 오류를 분석하기에 좋다.

<br><br><br><br>
reference) Effective Java 3/E, Joshua Bloch 