# item 18, 상속보다는 컴포지션을 사용하라

> 여기서 말하는 **상속의 의미**는 클래스가 다른 클래스를 확장하는 구현 상속을 의미한다. 클래스가 인터페이스를 구현하거나 인터페이스가 다른 인터페이스를 확장하는 인터페이스 상속과는 무관하다.

<br>

## 메서드 호출과 달리 상속은 캡슐화를 깨뜨린다.
- 캡슐화를 지키기 위한 방법은 아래와 같다.[^1]
  - 클래스 멤버 변수의 접근제어자를 private으로 선언한다.
  - 멤버 변수에 접근하기 위해 public setter and getter 메서드를 제공한다.
- 상속은 캡슐화를 깨뜨린다.
  - 상위 클래스가 어떻게 구현되느냐에 따라 하위 클래스의 동작에 이상이 생길 수 있다.
  - 상위 클래스는 릴리즈마다 내부 구현이 달라질 수 있으며, 그 여파로 코드 한 줄 건드리지 않은 하위 클래스가 오동작할 수 있다는 뜻이다.
  - 이러한 이유로 상위 클래스 설계자가 확장을 충분히 고려하고 문서화도 제대로 해두지 않으면, 하위 클래스는 상위 클래스의 변화에 발맞춰 계속 수정돼야만 한다.
  - 아래 예제 코드를 살펴보도록 하자.

```java
public class InstrumentedHashSet<E> extends HashSet<E> {  
    private int addCount = 0;

    public InstrumentedHashSet() {}
    
    @Override  
    public boolean add(E e) {  
        addCount++;  
        return super.add(e);  
    }  
  
    @Override  
    public boolean addAll(Collection<? extends E> c) {  
        addCount+= c.size();  
        return super.addAll(c);  
    }  
  
    public int getAddCount() {  
        return addCount;  
    }  
  
    public static void main(String[] args) {  
        InstrumentedHashSet s = new InstrumentedHashSet<>();  
        s.addAll(Arrays.asList("A", "B", "C"));  
        System.out.println(s.getAddCount()); // 6
    }  
}
```

<br>

## 그렇다면 어떻게 해결할 수 있을까?
1. addAll() 을 재정의하지 않기.
	- HashSet의 addAll() 이 add() 를 이용해 구현했음을 가정한 해법이라는 한계를 갖는다.
	- addAll()은 릴리즈 시점에 언제든 바뀔 수 있다.
2. addAll() 을 super를 사용하지 않도록 다시 재정의하기.
   - 비효율적이다.
   - 만약 HashSet의 private 필드를 써야하는 상황이라면 구현 자체가 불가능하다.
3. addAll() 을 재정의하는 대신 새로운 메서드를 추가하면 어떨까?
   - 다음 릴리즈때 상위 클래스에 추가된 새 메서드가 하필 내가 추가한 메서드와 시그니처가 같고 반환 타입은 다르다면 컴파일조차 되지 않을 것이다.
   - 반환 타입마저 같다면, 재정의한 꼴이 되니까 앞서의 문제와 똑같은 상황에 부딪힐 것이다.

위에서 언급한 문제를 모두 피해가면서 이러한 문제를 해결해줄 방법이 바로 '컴포지션'이다.

<br>

## 컴포지션(composition) 이란?
- **기존 클래스를 확장하는 대신, 새로운 클래스를 만들고 private 필드로 기존 클래스의 인스턴스를 참조하게 하는 설계 방식.**
- 기존 클래스가 새로운 클래스의 구성요소(composition) 로 쓰인다는 뜻에서 이러한 설계를 컴포지션이라고 부른다.
- 새 클래스의 인스턴스 메서드들은 private 필드로 참조하는 기존 클래스의 대응하는 메서드를 호출하여 그 결과를 반환한다. 이 방식은 전달(forwarding) 이라고 하며, 새 클래스의 메서드들을 전달 메서드(forwarding method) 라고 부른다.
- 그 결과 새로운 클래스는 기존 클래스의 내부 구현 방식의 영향에서 벗어날 수 있고, 기존 클래스에 새로운 메서드가 추가되더라도 전혀 영향받지 않는다.

```java
public class NewInstrumentedHashSet<E> {  
    private int addCount = 0;  
    private HashSet<E> s; // composition  
  
    public NewInstrumentedHashSet(HashSet<E> s) {  
        this.s = s;  
    }  
  
    // forwarding method  
    public boolean add(E e) {  
        addCount++;  
        return s.add(e); // forwarding  
    }  
  
    // forwarding method  
    public boolean addAll(Collection<? extends E> c) {  
        addCount += c.size();  
        return s.addAll(c); // forwarding  
    }   
  
    public int getAddCount() {  
        return addCount;  
    }  
  
    public static void main(String[] args) {  
        NewInstrumentedHashSet s = new NewInstrumentedHashSet<>(new HashSet<String>());  
        s.addAll(Arrays.asList("A","B","C"));  
        System.out.println(s.getAddCount()); // 3  
    }  
}
```
- 다른 Set 인스턴스를 감싸고(Wrap) 있다는 뜻에서 InstrumentedSet같은 클래스를 래퍼 클래스라 한다.
- 다른 Set에 계측 기능을 덧씌운다는 뜻에서 데코레이터 패턴이라고 한다.

<br>

## 그렇다면 래퍼 클래스는 항상 좋은 대안인가?
- 콜백 프레임워크와는 어울리지 않는다.

<br>

## 핵심 정리
- 상속은 강력하지만, 캡슐화를 해친다는 문제가 있다.
  - 클래스 B가 클래스 A와 Is-a 관계일 때만 클래스 A를 상속하라.
  - e.g) Student is a Person. class Student extends Person
    - is-a 관계일 때도 하위 클래스의 패키지가 상위 클래스와 다르고, 상위 클래스가 확장을 고려해 설계되지 않았다면 여전히 문제가 될 수 있다.
- 상속의 취약점을 피하려면 상속대신 컴포지션과 전달을 사용하자.

<br><br><br>
[^1]: java encapsulation, https://www.tutorialspoint.com/java/java_encapsulation.htm