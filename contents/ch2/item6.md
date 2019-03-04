## Item.6 불필요한 객체 생성을 피하라

<br>

Q. 어떤 것이 더 성능이 좋을까?  
```java
String BIKINI = new String("bikini"); // 1

String BIKINI = "bikini"; // 2
```
A. 2번
- 하나의 String 인스턴스를 사용한다.
- 그리고, 같은 JVM 안에서 이와 똑같은 문자열 리터럴을 사용하는 모든 코드가 같은 객체를 **재사용함**이 보장된다.
- [reference](https://docs.oracle.com/javase/specs/jls/se11/html/jls-3.html#jls-3.10.5)

<br>
<hr>

Q. 어떤 것이 더 성능이 좋을까?
```java
public class RomanNumerals {
    // 1
    static boolean isRomanNumeral(String s) {
        return s.matches("^(?=.)M*(C[MD]|D?C{0,3})" + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");
    }
    
    // 2
    private static final Pattern ROMAN = Pattern.compile("^(?=.)M*(C[MD]|D?C{0,3})" + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");
    static boolean isRomanNumeral(String s) {
        return ROMAN.matcher(s).matches();
    }
}
```
A. 2번
- String.matches() 는 정규 표현식으로 문자열 형태를 확인하는 가장 쉬운 방법이지만, 성능이 중요한 상황에서 반복해 사용하기엔 적합하지 않다.
- String.matched() 가 내부에서 만드는 정규표현식용 Pattern 인스턴스는 한 번 쓰고 버려져서 곧바로 가비지 컬렉션 대상이 되기 때문이다.
- 2번 방식으로 했을 때, isRomanNumeral()이 호출되지 않으면 ROMAN이 의미없이 초기화되지만, 지연 초기화등을 쓰길 권하진 않는다. 코드만 복잡해지고 성능은 크게 개선되지 않을 때가 많기 때문이다.

<br>
<hr>

Q. 어떤 문제가 있을까?
```java
private static long sum() {
    Long sum = 0L;
    for (long i = 0; i <= Integer.MAX_VALUE; i++) {
        sum += i;
    }
    return sum;
}
```
A. 박싱된 기본 타입보다는 기본 타입을 사용하고, 의도치 않은 오토박싱이 숨어들지 않도록 주의하자.

<br>
<hr>

### 요약
- 똑같은 기능의 객체를 매번 생성하기 보다는 객체 하나를 재사용하는 편이 나을 때가 많다. 재사용은 빠르고 세련되다. 특히, 불변 객체는 언제든 재사용할 수 있다. - p31
- '객체 생성은 비싸니 피해야 한다' 가 아니다. '기존 객체를 재사용해야 한다면 새로운 객체를 만들지 마라' 이다. 이 차이를 이해하도록 하자. - p34
- 방어적 복사가 필요한 상황에서 객체를 재사용했을 때의 피해가, 필요 없는 객체를 반복 생성했을 때의 피해보다 훨씬 크다는 사실을 기억하자. - p35