# Item 83. 지연 초기화는 신중히 사용하라

### 지연 초기화
- 지연 초기화는 필드의 초기화 시점을 그 값이 처음 필요할 때까지 늦추는 기법
- 따라서 값이 전혀 쓰이지 않으면 초기화도 결코 일어나지 않는다
- 지연 초기화는 주로 최적화 용도로 쓰이지만, 클래스와 인스턴스 초기화 때 발생하는 위험한 순환 문제를 해결하는 효과가 있다
- 다른 최적화와 마찬가지로 지연 초기화 역시 필요할 때까지는 하지 말자.


### 지연 초기화는 양날의 검
- 클래스 혹은 인스턴스 생성 시의 초기화 비용은 줄지만, 지연 초기화하는 필드에 접근하는 비용이 커짐
- 지연 초기화하려는 필드들 중 초기화가 이뤄지는 비율, 드는 비용, 호출 빈도수에 따라 성능이 느려질 수 있다!

### 지연 초기화가 필요한 경우
- 해당 클래스의 인스턴스 중 그 필드를 사용하는 인스턴스의 비율이 낮은 반면, 필드를 초기화하는 비용이 큰 경우
- 안타깝지만 위의 경우는 성능 측정을 통해 확인 가능하다
- 대부분의 상황에서 일반적인 초기화가 지연 초기화보다 낫다

### 멀티스레드 환경의 지연 초기화
- 까다롭다


#### 일반적인 초기화  
``
private final FieldType field = computeFieldValue();
``

#### synchronized 접근자 방식의 지연 초기화
지연 초기화가 초기화 순환성을 깨뜨릴 것 같으면 synchronized를 단 접근자를 사용.
 ```java
private final FieldType field;
private synchronized FieldType getField() {
    if(field == null)
        field = computeFieldValue();
    return field;
}
  ```
(위 두 초기화는 정적 필드에도 똑같이 적용됨, 필드와 접근자 메서드 선언에 static 한정자를 추가해야함)

#### 성능 때문에 정적 필드를 지연 초기화해야하는 경우
지연 초기화 홀더 클래스
 ```java
private static class FieldHolder {
    static final FieldType field = computeFieldValue();
}

private static FieldType getField() {
    return FieldHolder.field;
}
  ```
 
####성능 때문에 인스턴스 필드를 초기화해야 하는 경우 
**이중 검사 관용구**
- result라는 변수는 이미 초기화된(일반적인) 상황에서 그 필드를 딱 한번만 읽도록 보장해줌
- 반드시 필요하지 않지만 성능을 높여줌
 ```java
private volatile FieldType field;

private FieldType getField() {
  FieldType result = field;
  if(result != null) { // 첫 번 째 검사(락 사용안함)
      return result;
  }
  
  synchronized(this) {
      if(field == null)  //두 번째 검사(락 사용)
          field = computeFieldValue();
      return field;
  }
}
 ```

**단일 검사 관용구** 
- 반복해서 초기화해도 상관없는 인스턴스 필드를 지연 초기화해야 하는 경우, 이중검사에서 두 번째 검사 생략
```java
private volatile FieldType field;

private FieldType getField() {
  FieldType result = field;
  if(result == null) 
      field = result = computeFieldValue();
  return result;
}
 ```
 
- 모든 스레드가 필드의 값을 다시 계산해도 상관없고 필드의 타입이 long과 double을 제외한 다른 기본 타입이라면, 단일검사의 필드 선언시 volatile을 없애도 됨
(짜릿한 단일 검사로 거의 사용하지 않음)

### 결론
- 대부분의 필드는 지연시키지 말고 곧바로 초기화 하자
- 성능, 초기화 순환을 막기 위해 지연 초기화를 사용해야한다면, 올바른 지연 초기화 기법을 사용하자.



 
 