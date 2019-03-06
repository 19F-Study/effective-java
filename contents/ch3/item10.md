# 아이템10. equals는 일반 규약을 지켜 재정의하라

### equals의 재정의
**재정의 하지 않는것이 최선인 경우**
1. 각 인스턴스가 본질적으로 고유하다  
2. 인스턴스의 논리적동치성을 검사할 일이 없다  
3. 상위 클래스에서 재정의한 equals가 하위 클래스에도 딱 들어맞는다  
4. 클래스가 private이거나 package-private이고 equal 메서드를 호출할 일이 없다  

그렇다면 언제 재정의 해야할까?
- 객체 식별성이 아니라 논리적 동치성을 확인해야 하는데, 상위 클래스의 equals가 논리적 동치성을 비교하도록 재정의되지 않았을 때  
주로 Integer, String과 같은 값 클래스들이 해당, 단 값 클래스여도, 값이 같은 인스턴스가 둘 이상 만들어지지 않음을 보장하는 인스턴스 통제 클래스라면 재정의 하지 않아도됨
(대표적으로 Enum)
    
    
### Equal 재정의시 반드시 따라야 하는 일반 규약(Objects 명세에 적힌 규약)
> equals 메서드는 동치관계를 구현하며 다음을 만족한다

1. 반사성(reflexivity): null이 아닌 모든 참조 값 x에 대해, x.equals(x) 는 true
2. 대칭성(symmetry):null이 아닌모든 참조 값 x,y에 대해  x.equals(y)가  true이면   y.equals(x)도 true(예제코드 이해하기)
3. 추이성(transitivity):null이 아닌 모든 참조 값 x, y, z에 대해 x.equals(y)가 true이고, y.equals(z)가 true이면, x.equals(z)가 true이다(예제코드 이해)
4. 일관성(consistency):null이 아닌 모든 참조 값 x,y에 대해, x.equals(y)를 반복해서 호출하면 항상 true/false를 반환한다
5. null-아님:null이 아닌 모든 참조 값 x에 대해, x.equals(null)은 false

* 주의  
**equals 규약을 어기면 그 객체를 사용하는 다른 객체들이 어떻게 반응할지 알 수 없다**

리스코프 치환 원칙 : 
어떤 타입에 있어 중요한 속성이라면 그 하위 타입에서도 마찬가지로 중요하다   
따라서 그 타입의 모든 메서드가 하위 타입에서도 똑같이 잘 작동해야 한다

### 양질의 equals 메서드 구현 방법
1. == 연산자를 사용해 입력이 자기 자신의 참조인지 확인한다
2. instanceof 연산자로 입력이 올바른 타입이지 확인한다
3. 입력을 올바른 타입으로 형변환한다(2번으로 인해 100%성공)
4. 입력 객체와 자기 자신의 대응되는 핵심 필드들이 모두 일치하는지 하나씩 검사

비교시,  
float와 double을 제외한 기본 타입 필드는 ==연산자로 비교, 참조 타입은 equals 메서드로, float와 double은 Float.compare(), Double.compare()
더불어 null도 정상 값인 경우 Objects.equals로 비교하여 npe를 예방해야한다.


### equals의 성능
어떤 필드를 먼저 비교하느냐에 따라 성능이 좌우되기도 한다  
따라서 최상의 성능을 바라는 경우 다를 가능성이 더 크거나 비교하는 비용이 싼 필드를 먼저 비교하자

### equals 사용시
* equals를 구현한 후 대칭성, 추이성, 일관적인지에 대해 생각해보고 관련 단위테스트를 작성해본다
* Object 외의 타입을 매개변수로 받는 equals 메서드는 선언하지 말자   
  public boolean equals(MyCalss o) { … } -> 재정의가 아닌 다중정의
  
### 정리
1. 꼭 필요한 경우가 아니라면 equals를 재정의 하지 말자
- 많은 경우 Object의 equals가 원하는 비교를 정확히 해주기 때문이다
2. 만약 재정의를 해야한다면 해당 클래스의 핵심 필드 모두를 빠짐없이 다섯 가지 규약에 맞게 비교해야 한다.  

[참고]AutoValue  프레임워크  
    https://mincong-h.github.io/2018/08/21/why-you-should-use-auto-value-in-java/
