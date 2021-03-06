# item71. 필요 없는 검사 예외 사용은 피하라  
## 검사 예외
* 발생한 문제를 프로그래머가 처리하여 안전성을 높이고 프로그램의 질을 높일 수 있다.  
* 과하게 사용한 경우 예외 처리가 강제되기 때문에 사용하기 불편한 API가 된다.
* 검사 예외를 던지는 메서드는 스트림 안에서 직접 사용할 수 없기 때문에 자바8 부터는 부담이 더욱 커졌다. 
* API를 제대로 사용해도 발생할 수 있는 예외이거나, 프로그래머가 의미 있는 조치를 취할 수 있는 경우에만 검사 예외를 사용하자.

## 검사 예외를 회피하는 방법
1. 적절한 결과 타입을 담은 옵셔널을 반환한다. (검사 예외를 던지는 대신 빈 옵셔널을 반환)  
-> 단점) 예외가 발생한 이유를 알려주는 부가 정보를 담을 수 없다.
2. 검사 예외를 던지는 메서드를 2개로 쪼개 비검사 예외로 바꾼다.  
   첫 번째 메서드는 예외가 던져질 지 여부를 boolean으로 반환한다.  
```java
// 검사 예외를 던지는 메서드
try {
	obj.action(args);
} catch (TheCheckedException e) {
	// 예외 상황에 대처
}
```

```java
// 상태 검사 메서드와 비검사 예외를 던지는 메서드
if (obj.actionPermitted(args)) {
	obj.action(args);
} else {
	// 예외 상황에 대처한다.
}

// 실패 시 스레드를 중단하길 원한다면 다음 처럼 사용해도 된다. 
obj.action(args);
```

* 모든 상황에서 적용 할 수 있는 방법은 아니다.    
* 상태 검사 메서드를 사용하므로, 외부 동기화 없이 여러 스레드가 동시에 접근할 수 있거나 외부 요인에 의해 상태가 변할 수 있다면 적절하지 않다.
* 상태검사 메서드가 실제 작업의 일부를 중복 수행해야 한다면 성능에서 손해이므로 적잘하지 않다.

## 요약
* 꼭 필요한 곳에만 사용한다면 검사 예외는 프로그램의 안전성을 높여주지만 남용하면 사용성이 나빠진다.  
* API 호출자가 예외 상황에서 복구 할 방법이 없다면 비검사 예외를 던지자.  
* 복구 가능하고 호출자가 그 처리를 해주길 바란다면, 우선 옵셔널을 반환해도 될지 고민하자.  
* 옵서널만으로 상황을 처리하기에 충분한 정볼르 제공할 수 없을 때만 검사 예외를 사용하자.