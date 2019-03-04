## Item.5 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라

<br>

### 자원을 직접 명시하지 마라
많은 클래스가 하나 이상의 자원에 의존한다. 그 자원이 변하지 않고 모든 상황에서 동일하게 사용되는 자원이라면, 직접 명시해도 괜찮을 것이다.  
```java
private static final Logger logger = LogManager.getLogger();
```
하지만, 클래스가 의존하는 자원이 변할 수 있다면 자원을 직접 명시하는 것은 클래스의 유연성, 재사용성, 테스트 용이성을 저하시킨다. 예제를 살펴보도록 하자.  
SpellChecker 클래스는 맞춤법을 검사하는 유틸리티 클래스이다.   
```java
// 정적 유틸리티 클래스
public class SpellChecker {
    private static final Lexicon dictionary = new XXDictionary();
    
    private SpellChecker {} // 객체 생성 방지
    
    public static boolean isValid(String word) {...}
    
    public static List<String> suggestions(String typo) {...}
}

// 싱글턴 클래스
public class SpellChecker {
    private final Lexicon dictionary = new XXDictionary();
    
    private SpellChecker(...) {}
    
    public static SpellChecker INSTANCE = new SpellChecker(...);
    
    public boolean isValid(String word) {...}
    
    public List<String> suggestions(String type) {...}
    
}
``` 
두 방식 모두 dictionary 자원을 직접 명시하고 있다. 하지만, 실전에서는 다양한 사전을 지원해야 할 필요가 있을 수 있다.  
위 두 클래스에 있는 dictionary에 final을 제거하고 set 메서드를 추가할 수도 있지만, 이 방식은 어색하고 오류를 내기 쉬우며 멀티스레드 환경에서는 쓸 수 없다. (모두가 공유하는 클래스 / 인스턴스의 자원을 함부로 변경해서는 안된다.)  

결론적으로, 사용하는 자원에 따라 동작이 달라지는 클래스에 대해서는, 정적 유틸리티 클래스나 싱글턴 방식이 적합하지 않다. 대신 클래스가 여러 자원 인스턴스를 지원해야 하며, 클라이언트가 원하는 자원을 사용해야 한다.  
이 조건을 만족하는 간단한 패턴은 바로 인스턴스를 생성할 때 생성자에 필요한 자원을 넘겨주는 의존 객체 주입 패턴이다.

<br>

### 의존 객체 주입을 사용하라

```java
public class SpellChecker {
    private final Lexicon dictionary;
    
    // 의존 객체 주입 패턴
    public SpellChecker(Lexicon dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary);
    }
    
    public boolean isValid(String word) {...}
    
    public List<String> suggestions(String type) {...}
}
```

위 예에서는 딱 하나의 자원 (dictionary)만을 사용하지만, 자원이 몇 개든 의존 관계가 어떻든 상관없이 잘 작동한다.  
의존 객체 주입은 생성자, 정적 팩터리, 빌더 모두에 똑같이 응용할 수 있다.
불변을 보장하기 때문에 (final) 여러 클라이언트가 의존 객체를 안심하고 공유할 수 있다.  
```java
// e.g) SpellChecker 클래스 안에서 dictionary가 불변이기 때문에 client 생성 시에 dictionary를 공유할 수 있다.
Lexicon dictionary = new TestDictionary();

SpellChecker checker1 = new SpellChecker(dictionary);

SpellChecker checker2 = new SpellChecker(dictionary);

```

<br>

### 요약
- 클래스가 내부적으로 하나 이상의 자원에 의존하고, 그 자원이 클래스 동작에 영향을 준다면 싱글턴과 정적 유틸리티 클래스는 사용하지 않는 것이 좋다.
- 대신 필요한 자원을 생성자/정적 팩터리/빌더에 넘겨주자. 의존 객체 주입이라 하는 이 기법은 클래스의 유연성 재사용성, 그리고 테스트 용이성을 개선해준다.