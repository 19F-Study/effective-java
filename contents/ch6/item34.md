# 아이템 34. int 상수 대신 열거 타입을 사용하라

열거 타입
> 열거 타입은 일정 개수의 상수 값을 정의한 다음, 그 외의 값을 허용하지 않는 타입이다.


### 정수 열거 패턴(int enum pattern) 기법의 단점
     
```java
   public static final int APPLE_FUJI = 0;
   public static final int ORANGE_NAVEL = 0;
   
   public static final String APPLE = "APPLE";
 ``` 
- 타입 안전을 보장할 수 없다
- 표현력이 좋지 않다
- 평범한 상수를 나열한 것뿐이라 값이 깨지기 쉽다
- 정수 상수는 문자열로 출력하기가 까다롭다
- 같은 열거 그룹에 속한 모든 상수를 한 바퀴 순회하는 방법도 마땅치 않다
- 문자열 상수를 사용하는 변형 패턴(string enum pattern) 
    - 상수의 의미를 출력할 수 있지만, 하드코딩한 문자열에 오타가 있어도 확인할 길이 없다
    - 문자열 비교에 따른 성능 저하가 발생한다
    
### 열거 타입(enum type)
```java
public enum Apple { FUJI, PIPPIN, GRANNY_SMITH }
public enum Orange { NAVEL, TEMPLE, BLOOD}
 ``` 
- 자바의 열거 타입은 완전한 형태의 클래스
- 열거 타입 자체는 클래스이며, 상수 하나당 자신의 인스턴스를 하나씩 만들어 public static final 필드로 공개
- 열거 타입은 밖에서 접근할 수 있는 생성자를 제공하지 않으므로 사실상 final
- 따라서 열거 타입 선언으로 만들어진 인스턴스들은 클라이언트가 직접 생성하거나 확장할 수 없으므로 딱 하나씩만 존재하는 싱클턴
- 열겨 타입은 컴파일 타임 타입 안전성을 제공
- 열거 타입에는 메서드나 필드를 추가할 수 있고, 임의의 인터페이스를 구현할 수도 있다(Obect, Comparable, Serializable)
- 열거 타입 상수 각각을 특정 데이터와 연결지으려면 생성장에서 데이터를 받아 인스턴스 필드에 저장
- 널리 쓰이는 열거 타입은 톱 레벨 클래스로 만들고, 특정 톱레벨 클래스에서만 쓰인다면 해당 클래스의 멤버 클래로 만든다 
- 열거 타입은 상수는 생성자에서 자신의 인스턴스를 맵에 추가할 수 없다
    - 열거 타입의 생성자에서 접근할 수 있는 것은 상수 변수뿐
    - 열거 타입 생성자가 실행되는 시점에는 정적 필드들이 아직 초기화되기 전이므로
    
### 상수마다 동작이 달려져야 하는 상황
1. switch문을 이용한 분기
    - 열거타입이 추가될때마다 분기가 필요
2. 상수별 메서드 구현 
    - 추상 메서드를 선언 후 , 상수별 클래스 몸체(각 상수)에서 자신에 맞게 재정의하는 법 (예제 확인)
    - 기존 열거 타입에 상수별 동작을 혼합해 넣을 때는 switch문이 좋은 선택이 될 수 있다.
    - 단점
        - 상수별 메서드 구현에는 열거 타입 상수끼리 코드를 공유하기 어렵다   
        - switch 문을 이용해 상수별 동작을 구현하는데 적합하지 않다  
3. 상수 추가시 전략을 선택하도록 구현
    - 열거 타입 상수 일부가 같은 동작을 공유하는 경우에 적합

### 정리
1. 필요한 원소를 컴파일 타임에 다 알 수 있는 상수 집합이라면 항상 열거 타입을 사용하자
2. 열거 타입에 정의된 상수 개수가 영원히 고정 불변일 필요는 없다(나중에 상수가 추가돼도 바이너리 수준에서 호환되도록 설계)
3. 정수 상수보다 열거 타입이 더 뛰어나다
    - 읽기 쉽고, 안전하고 강력하다

   