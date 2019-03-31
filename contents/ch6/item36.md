# 아이템 36. 비트 필드 대신 EnumSet을 사용하라
- 열거한 값들이 주로(단독이 아닌) 집합으로 사용될 경우, 기존에는 각 상수에 서로 다른 2의 거듭제곱 값을 할당한 정수 열거 패턴을 사용했다.
- 다음과 같이 비트별 OR를 사용해 여러 상수를 하나의 집합으로 모을 수 있으며, 이렇게 만들어진 집합을 비트 필드(bit field)라고 한다.
    - text.applyStyles(STYLE_BOLD | STYLE_ITALIC)
```java
   public class Text {
    public static final int STYLE_BOLD          = 1 << 0; //1
    public static final int STYLE_ITALIC        = 1 << 1; //2
    public static final int STYLE_UNDERLINE     = 1 << 2; //4
    public static final int STYLE_STRIKETHROUGH = 1 << 3; //8
                
    public void applyStyles(int styles) {
        ...
    }
   }
 ``` 
### 비트 필드 
- 비트 필드를 사용하면 비트별 연산을 사용해 합집하과 교집합 같은 집합 연산을 효율적으로 수행할 수 있다. 
- 비트 필드는 정수 열거 상수의 단점을 그대로 가지고 있다.
- 추가적인 문제
    1. 비트 필드 값이 그대로 출력되면 단순한 정수 열거 상수를 출력할 때보다 해석하기가 훨씬 어렵다
    2. 비트 필드 하나에 녹아 있는 모든 원소를 순회하기도 까다롭다
    3. 최대 몇 비트가 필요한지 API 작성 시 미리 예측하여 적절한 타입(int/long)을 선택해야 한다
        - API를 수정하지 않고 비트수(32bit/64bit)를 더 늘릴 수 없기 때문   
- 상수 집합을 주고받아야 할때 비트 필드를 사용하는 프로그래머도 있다.

### 대안
- java.util패키지의 EnumSet 클래스를 사용하자

### EnumSet
- EnumSet 클래스는 열거 타입 상수의 값으로 구성된 집합을 효과적으로 표현해준다.
- Set 인터페이스를 완벽히 구현하며, 타입 safe하고 다른 어떤 Set 구현체와도 함께 사용할 수 있다.
- EnumSet 내부는 비트 벡터로 구현되어 있다. 
    - 원소가 총 64개 이하라면, 대부분의 경우에 EnumSet 전체를 long 변수 하나로 표현하여 비트 필드에 비견되는 성능을 보여준다.
    - removeAll, retainAll 같은 대량 작업은비트를 효율적으로 처리할 수 있는 산술 연산을 써서 구현했다.
    - bit를 직접 다룰 때 흔히 겪는 오류들을 EnumSet이 다 처리해준다.
    
```java
   public class Text {
    public enum Style{
        BOLD, ITALIC, UNDERLINE, STRIKETHROUGH
    }
                
    //어떤 Set을 넘겨도 좋지만, EnumSet 이 가장 좋다!
    public void applyStyles(Set<Style> styles) {
        ...
    }
   }
 ```
- text.applyStyles(EnumSet.of(Style.BOLD, Style.ITALIC)) 과 같이 사용한다 
    - EnumSet은 집합 생성 등 다양한 기능의 정적 팩토리를 제공하는데 이중 of를 사용
- public void applyStyles(Set<Style> styles)에서 왜 EnumSet<Style> 대신 Set<Style>을 받았을까?
    - 모든 클라이언트가 EnumSet을 건넬 것이라고 짐작이 되어도, 이왕이면 인터페이스로 받는게 일반적으로 좋은 습관이다.
    - 클라이언트가 다른 Set 구현체를 넘기더라도 처리할 수 있기 때문
**현재 EnumSet의 유일한 단점은 불변 EnumSet을 만들 수 없다는 것(자바 9까지는), 따라서 해당 버전 이하의 경우 Collections.unmodifiableSet으로 감싸서 사용하는 것을 권장한다.**
