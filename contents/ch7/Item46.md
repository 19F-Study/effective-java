# item46. 스트림에는 부작용 없는 함수를 사용하라  
## 스트림
* 함수형 프로그래밍에 기초한 패러다임으로, 스트림 패러다임의 핵심은 계산을 일련의 변환으로 재구성 하는 부분이다.  
* 각 변환 단계는 가능한 한 이전 단계의 결과를 받아 처리하는 순수 함수여야 한다.  
* 순수함수란 오직 입력만이 결과에 영향을 주는 함수이다. 다른 가변 상태를 참조하지 않고, 함수 스스로도 다른 상태를 변경하지 않는다.  
* 스트림 연산에 건네는 함수 객체는 모두 부작용(side effect)이 없어야 한다.  
* forEach 연산은 스트림 계산 결과를 보고할 때만 사용하고, 계산하는데는 쓰지 말자.  

## 수집기 (Collector)  
* 수집기를 사용하면 스트림의 원소를 손쉽게 컬렉션으로 모을 수 있다.  
  - toList() : List를 반환  
  - toSet()  : Set을 반환  
  - toMap() : Map을 반환
  - toCollection(collectionFactory) : 프로그래머가 지정한 컬렉션 타입을 반환  
  
> 참고  
[Java docs of Collectors](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Collectors.html)

## 수집기 팩터리

#### Collectors.groupingBy
```java
public static <T, K> Collector<T, ?, Map<K, List<T>>>
    groupingBy(Function<? super T, ? extends K> classifier) {
        return groupingBy(classifier, toList());
    }
```
분류함수(classifier) : key를 매핑한다.

#### Collectors.joining
```java
public static Collector<CharSequence, ?, String> joining() {
        return new CollectorImpl<CharSequence, StringBuilder, String>(
                StringBuilder::new, StringBuilder::append,
                (r1, r2) -> { r1.append(r2); return r1; },
                StringBuilder::toString, CH_NOID);
    }
```