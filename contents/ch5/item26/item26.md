# Item 26. 로 타입을 사용하지 말라.

> 클래스와 인터페이스 선언에 타입 매개변수가 쓰이면, 이를 제네릭 클래스 혹은 제네릭 인터페이스, 제네릭 타입이라 한다.

> 로 타입이란 제네릭 타입에서 타입 매개변수를 전혀 사용하지 않을 때를 말한다. 

## 로 타입은 제네릭이 도래하기 전 코드와 호환되도록 하기 위한 궁여지책이다.

```java
//Collection 에 Stamp 대신 Coin 을 넣어도 컴파일이 되며 실행이 된다. 
private final Collection stamps = ...;

//RunTime 에 Exception 발생.
for (Iterator i = stamps.iteration(); i.hasNext();) {
  Stamp stamp = (Stamp) i.next(); //ClassCastException
  stamp.cancel();
}
```

- 오류는 가능한 한 발생 즉시, 이상적으로는 컴파일할 때 발견하는 것이 좋다.
- 물리적으로 다른 타입을 넣는 코드가 떨어져 있다면 찾기 힘들다.
- 제네릭을 활용하면 타업 선언 자체가 코드에 녹아든다.

```java
private final Collection<Stamp> stamps = ...;
```



## List 같은 로 타입과 List<Object> 차이는?

- List 는 제네릭 타입에서 완전히 발을 뺀 것.
- List<Object> 는 모든 타입을 허용한다. 3판 p.156
- The former has opted out of the generic type system, while the latter has explicitly told the compiler that it is capable of holding objects of any type. 3판 영문버전 p.144
- 제네릭에 대한 하위 자료형 정의 규칙에 따르면 List<String>은 List의 하위 자료형(subtype) 이지만 List<Object> 의 하위 자료형은 아니기 때문이다. 2판 p.153
- List(로 타입) 를 매개변수로 받는 메서드에 List<String> 은 넘길 수 있지만, List<Object> 를 받는 메서드에는 넘길 수 없다.
- List<Object> 같은 매개변수화 타입을 사용할 때와 달리 List 같은 로 타입을 사용하면 타입 안정성을 잃게 된다.
- List<Object> => Object 타입만 받겠다! Object 상속 여부와는 관계없이!
- List<?> => 모든걸 받겠다!



## 원소의 타입을 몰라도 되는 로 타입을 쓰고 싶을때!

```java
static int numElementsInCommon(Set s1, Set s2) {
  int result = 0;
  for (Obejct o1 : s1) {
    if (s2.contains(o1)) {
      result++;
    }
  }
  return result;
}
```

- 동작은 하지만 로 타입을 사용해 안전하지 않다.
- 이럴 때는 비한정적 와일드카드 타입을 대신 사용하는게 좋다.

```java
static int numElementsInCommon(Set<?> s1, Set<?> s2) {
  
}
```

- 와일드카드 타입은 안전하고, 로 타입은 안전하지 않다.
- 로 타입 컬렉션에는 아무 원소나 넣을 수 있으니 타입 불변식을 훼손하기 쉽다.
- Collection<?> 에는 어떤 원소도 넣을 수 없다. 



## 로 타입을 사용해도 되는 경우!

- class 리터털에는 사용해야 한다.

- instanceof 를 사용할때!

  ```java
  //instanceof 사용 후에 Set<?>로 형변환해서 사용해야 컴파일러 경고가 안뜬다!
  if (o instanceof Set) {
    Set<?> s = (Set<?>) o;
  }
  ```

  