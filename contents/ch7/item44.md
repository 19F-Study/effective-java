# 표준 함수형 인터페이스를 사용하라

자바가 람다를 지원하면서 API를 작성하는 모범 사례도 크게 바뀌었다. 예컨대 상위 클래스의 기본 메서드를 재정의해 원하는 동작을 구현하는 템플릿 메서드 패턴의 매력이 크게 줄었다. 이를 대체하는 현대적인 해법은 **같은 효과의 함수 객체를 받는 정적 팩터리나 생성자를 제공하는 것이다.** 

```java
public abstract class Ramen {
  void cook() {
    boil();
    putRamen();
    doSomething();
  }
  abstract void boil();
  abstract void putRamen(); 
  abstract void doSomething();
}
```
```java
public class Ramen<T, R> {  
    private Function<T, R> cook;
    // 이때 함수형 매개변수 타입을 올바르게 선택해야 한다.
    public Ramen(Function<T, R> cook) { this.cook = cook; }  
    public R cook(T someRamen) { return cook.apply(someRamen); }  
}
```

<br>

## LinkedHashMap을 살펴보자
removeEldestEntry() 를 재정의하면 가장 최근 원소 N 개만을 유지하게 할 수 있다.
```java
public class NewLinkedHashMap<K,V> extends LinkedHashMap<K,V> {  
    private static final int LIMIT = 10;
    
    @Override  
    protected boolean removeEldestEntry(Map.Entry eldest) {  
        return size() > LIMIT;  
    }  
  
    public static void main(String[] args) {  
        NewLinkedHashMap<Integer, String> map = new NewLinkedHashMap<>();  
        for (int i = 0; i < 50; i++) {  
            map.put(i, String.valueOf(i));  
        }  
        System.out.println(map);  
    }  
}
```
잘 동작하긴 하지만, 이를 오늘날 다시 구현한다면 함수 객체를 받는 정적 팩터리나 생성자를 제공했을 것이다.
```java
@FunctionalInterface interface EldestEntryRemovalFunction<K, V> {
    boolean remove(Map<K, V> map, Map.Entry<K, V> eldest);
}
```
이 인터페이스도 잘 동작하지만, 굳이 사용할 이유는 없다. 자바 표준 라이브러리에 이미 같은 모양의 인터페이스가 준비되어있기 때문이다.

<br>

## java.util.function
- java.util.funcion 패키지를 보면 다양한 용도의 표준 함수형 인터페이스가 담겨있다.  
- [https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html](https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html)
- 필요한 용도에 맞는게 있다면, 직접 구현하지 말고 표준 함수형 인터페이스를 활용하라.
- 총 43개의 인터페이스가 담겨있어서 그 양이 많지만, 기본 인터페이스 6개만 기억하면 나머지를 충분히 유추해낼 수 있다.
- 이 기본 인터페이스들은 모두 참조 타입용이다.

**기본 인터페이스 종류**
- Operator Interface
	- 반환값과 인수의 타입이 같은 함수를 뜻한다.
	- 인수가 1개인 **UnaryOperator Interface**와 2개인 **BinaryOperator Interface**로 나뉜다.
- Predicate Interface
  - 인수 하나를 받아 boolean을 반환하는 함수를 뜻한다.
- Function Interface
  - 인수와 반환 타입이 다른 함수를 뜻한다.
- Supplier Interface
  - 인수를 받지 않고 값을 반환하는 함수를 뜻한다. 
- Consumer Interface
  - 인수를 하나 받고 반환값은 없는 함수를 뜻한다.

**기본 인터페이스 더 알아보기**
- primitive type인 int, long, double 용으로 각 3개씩 변형이 존재한다.
  - Function의 변형만 매개변수화 됐다. 즉, LongFunction<int[]>은 long 타입을 매개변수로 받는다는 의미이다.
- Function 인터페이스에는 기본 타입을 반환하는 변형이 총 9 개가 더 있다.
  - 입력과 결과 타입이 모두 기본 타입이라면 srcToResult 접두어를 사용한다. e.g) LongToIntFunction
  - 나머지는 입력이 객체참조이고 결과가 int, long, double인 변형들로, 앞서와 달리 입력을 매개변수화하고 접두어로 ToResult를 사용한다. e.g) ToLongFunction<int[]>
- 기본 함수형 인터페이스 중 BiPredicate<T, U> BiFunction<T, U, R> BiConsumer<T. U>는 인수를 2개씩 받는다.
- 기본 함수형 인터페이스 박싱된 기본 타입을 넣어 사용하지는 말자.
- 대부분의 상황에서는 직접 작성하는 것보다 표준 함수형 인터페이스를 사용하는 편이 낫다. 단, 검사 예외를 던진다든가, 매개변수를 더 받아야할 필요가 있다면 직접 작성해야 한다.

<br>

## 전용 함수형 인터페이스를 구현할 때 고려해야 할 점
- 자주 쓰이며, 이름 자체가 용도를 명확히 설명해준다. 
- 반드시 따라야 하는 규약이 있다.
- 유용한 디폴트 메서드를 제공할 수 있다.

**@FunctionalInterface Annotation을 사용하라**  
직접 만든 함수형 인터페이스에는 항상 @FunctionalInterface 애너테이션을 사용하라.
- 해당 클래스의 코드나 설명 문서를 읽을 이에게 그 인터페이스가 람다용으로 설계된 것임을 알려준다.
- 해당 인터페이스가 추상 메서드를 오직 하나만 가지고 있어야 컴파일되게 해준다. 
- 그 결과, 유지보수 과정에서 누간가 실수로 메서드를 추가하지 못하게 막아준다.

**함수형 인터페이스를 API에서 사용할 때의 주의점**  
서로 다른 함수형 인터페이스를 같은 위치의 인수로 받는 메서드들을 다중 정의해서는 안된다.
- 클라이언트에게 모호함을 안겨준다.
- ExecutorService의 submit에 Callable<T> 를 받는 것과 Runnable<T> 를 받는 것을 다중 정의했다. 이로 인해, 올바른 메서드를 알려주기 위해 형변환해야 할 때가 자주 발생하게된다.

<br>

## 요약
- 함수형 인터페이스 타입을 활용하라. 보통은 java.util.function 패키지의 표준 함수형 인터페이스를 사용하는 것이 가장 좋은 선택이다. 단, 흔치는 않지만 직접 새로운 함수형 인터페이스를 만들어 쓰는 편이 나을 수도 있음을 잊지 말자.